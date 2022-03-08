package graphql.models

import sangria.macros.derive._

import graphql.models.LongIdentifiable._
import graphql.models.ChatMessage._

case class ChatRoom(id: Long, name: String, messages: Seq[ChatMessage])
    extends LongIdentifiable

object ChatRoom {
  implicit val ChatRoomType =
    deriveObjectType[Unit, ChatRoom](Interfaces(LongIdentifiableType))
}
