package entities

import java.time.ZonedDateTime

case class ChatRoom(
    id: Long,
    name: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
) extends Entity
