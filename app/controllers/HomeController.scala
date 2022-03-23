package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl._
import controllers.websocket.WebSocketActor

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)(
    implicit
    system: ActorSystem,
    mat: Materializer
) extends BaseController {

  /** Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  implicit val messageFlowTransformer =
    MessageFlowTransformer.jsonMessageFlowTransformer[String, JsValue]

  def webSocket(): WebSocket = WebSocket.accept[String, JsValue] {
    implicit request =>
      ActorFlow.actorRef { out =>
        WebSocketActor.props(out)
      }

//      // Log events to the console
//      val in = Sink.foreach[JsValue](println)
//
//      // Send a single 'Hello!' message and then leave the socket open
//      val out = Source
//        .single(Json.toJson(Json.obj("hello" -> "hello")))
//        .concat(Source.maybe)
//
//      Flow.fromSinkAndSource(in, out)
  }
}
