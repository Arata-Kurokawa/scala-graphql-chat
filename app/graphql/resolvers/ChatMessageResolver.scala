package graphql.resolvers

import graphql.models.ChatMessage
import repositories.ChatMessageRepository

case class ChatMessageResolver() {
  private val chatMessageRepository: ChatMessageRepository =
    ChatMessageRepository()

  def add(chatRoomId: Long, message: String): ChatMessage = {
    val id = this.chatMessageRepository.add(chatRoomId, message)
    ChatMessage(id, chatRoomId, message)
  }
}

