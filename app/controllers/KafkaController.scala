/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers

import akka.actor.ActorSystem
import akka.kafka.ProducerMessage.MultiResultPart
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{
  ConsumerSettings,
  ProducerMessage,
  ProducerSettings,
  Subscriptions
}
import akka.stream.scaladsl.{Sink, Source}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{
  StringDeserializer,
  StringSerializer
}
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.mvc.InjectedController

import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import akka.kafka.scaladsl.Consumer.DrainingControl
import controllers.websocket.KafkaWebSocketActor

import scala.concurrent.duration.DurationInt

import java.time.LocalDateTime

class KafkaController @Inject() extends InjectedController {
  implicit val system = ActorSystem()

  def producer: Action[AnyContent] = Action.async { implicit request =>
    val config = system.settings.config.getConfig("our-kafka-producer")
    val producerSettings =
      ProducerSettings(config, new StringSerializer, new StringSerializer)

    val done = Source(1 to 1)
      .map { number =>
        val partition = 0
        val value = number.toString
        ProducerMessage.single(
          new ProducerRecord("test", partition, "key", value),
          number
        )
      }
      .via(Producer.flexiFlow(producerSettings))
      .map {
        case ProducerMessage.Result(
              metadata,
              ProducerMessage.Message(record, passThrough)
            ) =>
          s"${metadata.topic}/${metadata.partition} ${metadata.offset}: ${record.value}"

        case ProducerMessage.MultiResult(parts, passThrough) =>
          parts
            .map { case MultiResultPart(metadata, record) =>
              s"${metadata.topic}/${metadata.partition} ${metadata.offset}: ${record.value}"
            }
            .mkString(", ")

        case ProducerMessage.PassThroughResult(passThrough) =>
          s"passed through"
      }
      .runWith(Sink.foreach(println(_)))

    for {
      _ <- done
    } yield {
      Ok(Json.toJson(Json.obj("test" -> "test")))
    }
  }

  def consumer() = WebSocket.accept[String, String] { implicit request =>
    ActorFlow.actorRef { out =>
      val consumerConfig =
        system.settings.config.getConfig("our-kafka-consumer")

      val consumerSettings =
        ConsumerSettings(
          consumerConfig,
          new StringDeserializer,
          new StringDeserializer
        ).withGroupId(LocalDateTime.now().toString)

      val topicSource =
        Consumer
          .plainSource[String, String](
            consumerSettings,
            Subscriptions.topics("test")
          )
          .map(consumerRecord => consumerRecord.value)

      val control = topicSource
        .toMat(Sink.foreach(m => {
          println(
            "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
          )
          out ! m
        }))(DrainingControl.apply)
        .run()

      // websocketの接続維持のためメッセージ送信
      // 本来はtypeなどを持ったjsonを返しクライアントでフィルターする想定
      val ping = Source
        .tick(
          30.second, // delay of first tick
          30.second, // delay of subsequent ticks
          "ping" // element emitted each tick
        )
        .to(Sink.foreach(m => { out ! m }))
        .run()

      KafkaWebSocketActor.props(out, control, ping)
    }
  }
}
