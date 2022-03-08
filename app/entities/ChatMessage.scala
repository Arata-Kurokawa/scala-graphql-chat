package entities

case class ChatMessage(
    id: Long,
    chatRoomId: Long,
    message: String
) extends Entity
