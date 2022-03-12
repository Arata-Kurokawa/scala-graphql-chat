package repositories.persistences.mysql

import java.time.ZonedDateTime
import scalikejdbc._

import entities.ChatMessage

case class ChatMessageDataModel(
  id: Long,
  chatRoomId: Long,
  message: String,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime
)

object ChatMessageTable extends SQLSyntaxSupport[ChatMessageDataModel] {
  override val tableName = "chat_messages"

  def apply(m: ResultName[ChatMessageDataModel])(rs: WrappedResultSet): ChatMessage = {
    ChatMessage(
      id = rs.long(m.id),
      chatRoomId = rs.long(m.chatRoomId),
      message = rs.string(m.message),
      createdAt = rs.zonedDateTime(m.createdAt),
      updatedAt = rs.zonedDateTime(m.updatedAt)
    )
  }
}
