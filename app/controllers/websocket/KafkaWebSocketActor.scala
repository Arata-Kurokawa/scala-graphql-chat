/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers.websocket

import akka.actor.{Actor, ActorRef, Cancellable, Props}
import akka.kafka.scaladsl.Consumer
import play.api.libs.json.Json

object KafkaWebSocketActor {
  def props(
      out: ActorRef,
      control: Consumer.Control,
      ping: Cancellable
  ) =
    Props(new KafkaWebSocketActor(out, control, ping))
}

class KafkaWebSocketActor(
    out: ActorRef,
    control: Consumer.Control,
    ping: Cancellable
) extends Actor {
  def receive = { case msg: String =>
    out ! Json.toJson(Json.obj("message" -> msg))
  }

  override def postStop(): Unit = {
    super.postStop()
    control.shutdown()
    ping.cancel()
  }
}
