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
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.ActorFlow
import play.api.mvc.{request, _}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import akka.kafka.scaladsl.Consumer.DrainingControl
import controllers.websocket.KafkaWebSocketActor
import play.api.mvc.WebSocket.MessageFlowTransformer

import scala.concurrent.duration.DurationInt
import java.time.LocalDateTime
import scala.concurrent.Future

class KafkaController @Inject() extends InjectedController {
  implicit val system = ActorSystem()

  def login: Action[AnyContent] = Action { implicit request =>
    println("----------------------------------------------------")
    println(request.session.get("_login"))
    NoContent.withSession(
      request.session + ("_login" -> "yes")
    )
  }

  def logout: Action[AnyContent] = Action { implicit request =>
    NoContent.withSession(
      request.session + ("_login" -> "no")
    )
  }

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

  def sendMessage() = Action(parse.json).async { implicit request =>
    val message = (request.body \ "message").as[String]

    val config = system.settings.config.getConfig("our-kafka-producer")
    val producerSettings =
      ProducerSettings(config, new StringSerializer, new StringSerializer)

    val done = Source(Seq(message))
      .map { m =>
        ProducerMessage.single(
          new ProducerRecord("test", "key", m),
          1
        )
      }
      .via(Producer.flexiFlow(producerSettings))
      .run()

    for {
      _ <- done
    } yield {
      NoContent
    }
  }

  implicit val messageFlowTransformer: MessageFlowTransformer[String, JsValue] =
    MessageFlowTransformer.jsonMessageFlowTransformer[String, JsValue]

  def consumer() = WebSocket.acceptOrResult[String, JsValue] {
    implicit request =>
      println("======================================================")
      println(request.session.get("_login"))
      Future.successful(
        request.session.get("_login") match {
          case Some(m) if m == "yes" =>
            Right(
              ActorFlow.actorRef { out =>
                // 設定値取得
                val consumerConfig =
                  system.settings.config.getConfig("our-kafka-consumer")

                // consumer設定
                val consumerSettings =
                  ConsumerSettings(
                    consumerConfig,
                    new StringDeserializer,
                    new StringDeserializer
                  ).withGroupId(LocalDateTime.now().toString)

                // kafkaを監視するSource
                val topicSource =
                  Consumer
                    .plainSource[String, String](
                      consumerSettings,
                      Subscriptions.topics("test")
                    )
                    .map(consumerRecord => consumerRecord.value)

                // 監視開始
                // メッセージを受信したらwebsocketに連携
                val control = topicSource
                  .toMat(Sink.foreach(m => {
                    println("++++++++++++++++++++++++++++++++++++++")
                    out ! Json.toJson(Json.obj("message" -> m))
                  }))(DrainingControl.apply)
                  .run()

                // websocketの接続維持のためメッセージ送信
                // 本来はtypeなどを持ったjsonを返しクライアントでフィルターする想定
                val ping = Source
                  .tick(
                    3.second, // delay of first tick
                    30.second, // delay of subsequent ticks
                    "ping" // element emitted each tick
                  )
                  .to(Sink.foreach(m => {
                    out ! Json.toJson(Json.obj("message" -> m))
                  }))
                  .run()

                KafkaWebSocketActor.props(out, control, ping)
              }
            )
          case _ => Left(Forbidden)
        }
      )
  }
}
