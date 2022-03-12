package entities

import java.time.ZonedDateTime

case class ChatMessage(
    id: Long,
    chatRoomId: Long,
    message: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
) extends Entity
