package graphql

import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import sangria.ast.Document
import sangria.parser.QueryParser

import scala.util.{Failure, Success, Try}

case class ParsedQuery(
  query: Document,
  operation: Option[String],
  variables: JsObject
)

object GraphqlParser {
  def parseQuery(jsValue: JsValue): Try[ParsedQuery] = {
    val query = (jsValue \ "query").as[String]
    val operation = (jsValue \ "operationName").asOpt[String]
    val variables = (jsValue \ "variables").toOption match {
      case Some(JsString(vars)) => parseVariables(vars)
      case Some(obj: JsObject)  => obj
      case _                    => Json.obj()
    }

    Try {
      QueryParser.parse(query) match {
        // query parsed successfully, time to execute it!
        case Success(queryAst) =>
          ParsedQuery(queryAst, operation, variables)

        // can't parse GraphQL query, return error
        case Failure(error) =>
          throw new Error(s"Fail to parse a request body. Reason [$error]")
      }
    }
  }

  private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj()
    else Json.parse(variables).as[JsObject]
}
