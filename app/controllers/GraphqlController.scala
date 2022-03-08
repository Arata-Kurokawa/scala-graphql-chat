package controllers

import play.api.mvc.{Action, AnyContent, InjectedController}
import sangria.marshalling.playJson._
import sangria.execution.Executor
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

import controllers.requests.TestRequest
import graphql.Schema
import graphql.resolvers.Resolvers

class GraphqlController @Inject() extends InjectedController {
  def get(): Action[AnyContent] = Action.async { implicit request =>
    Executor
      .execute(Schema.schema, TestRequest.chatRoomQuery2, new Resolvers())
      .map(Ok(_))
  }

  def post(): Action[AnyContent] = Action.async { implicit request =>
    Executor
      .execute(Schema.schema, TestRequest.chatRoomQuery2, new Resolvers())
      .map(Ok(_))
  }
}
