package controllers.websocket

import akka.actor._
import play.api.libs.json.Json

object WebSocketActor {
  def props(out: ActorRef) = Props(new WebSocketActor(out))
}

class WebSocketActor(out: ActorRef) extends Actor {
  def receive = { case msg: String =>
    out ! (Json.obj("message" -> msg))
  }
}
