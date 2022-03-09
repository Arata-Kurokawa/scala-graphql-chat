package controllers

import play.api.mvc.{Action, InjectedController, Result}
import sangria.marshalling.playJson._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.ast.Document

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import graphql.Schema
import graphql.resolvers.Resolvers
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import sangria.parser.{QueryParser}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class GraphqlController @Inject() extends InjectedController {
  def post: Action[JsValue] = Action.async(parse.json) { implicit request =>
    def parseVariables(variables: String) =
      if (variables.trim == "" || variables.trim == "null") Json.obj()
      else Json.parse(variables).as[JsObject]

    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").toOption match {
      case Some(JsString(vars)) => parseVariables(vars)
      case Some(obj: JsObject)  => obj
      case _                    => Json.obj()
    }

    QueryParser.parse(query) match {
      // query parsed successfully, time to execute it!
      case Success(queryAst) =>
        this.executeGraphQLQuery(queryAst, operation, variables)

      // can't parse GraphQL query, return error
      case Failure(error) =>
        Future.successful(BadRequest(Json.obj("error" -> error.getMessage)))
    }
  }

  private def executeGraphQLQuery(
      query: Document,
      op: Option[String],
      vars: JsObject
  ): Future[Result] =
    Executor
      .execute(
        Schema.schema,
        query,
        new Resolvers(),
        operationName = op,
        variables = vars
      )
      .map(Ok(_))
      .recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver  => InternalServerError(error.resolveError)
      }
}
