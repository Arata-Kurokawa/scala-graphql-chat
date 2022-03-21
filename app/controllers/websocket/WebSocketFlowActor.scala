package controllers.websocket

import akka.actor.{Actor, ActorRef, Props}

object WebSocketFlowActor {
  def props(outActor: ActorRef): Props = ???
}

class WebSocketFlowActor extends Actor {
  override def receive: Receive = ???
}