package controllers

import play.api.mvc.{Action, InjectedController, Result, WebSocket}
import sangria.marshalling.playJson._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.ast.Document

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import graphql.{GraphqlParser, Schema}
import graphql.resolvers.Resolvers
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class GraphqlController @Inject() extends InjectedController {
  def post: Action[JsValue] = Action.async(parse.json) { implicit request =>
    GraphqlParser.parseQuery(request.body) match {
      case Success(parsedQuery) =>
        this.executeGraphQLQuery(parsedQuery.query, parsedQuery.operation, parsedQuery.variables)
      case Failure(error) =>
        Future.successful(BadRequest(Json.obj("error" -> error.getMessage)))
    }
  }

  def webSocket(): WebSocket = ???

  private def executeGraphQLQuery(
      query: Document,
      op: Option[String],
      vars: JsObject
  ): Future[Result] =
    Executor
      .execute(
        Schema.schema,
        query,
        Resolvers(),
        operationName = op,
        variables = vars
      )
      .map(Ok(_))
      .recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver  => InternalServerError(error.resolveError)
      }
}
