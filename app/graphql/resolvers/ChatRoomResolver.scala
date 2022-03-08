/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package graphql.resolvers

import graphql.models.{ChatRoom, ChatMessage}
import repositories.{ChatMessageRepository, ChatRoomRepository}

class ChatRoomResolver {
  // TODO 依存注入
  private val chatRoomRepository: ChatRoomRepository = ChatRoomRepository()
  private val chatMessageRepository: ChatMessageRepository =
    ChatMessageRepository()

  def find(id: Long): Option[ChatRoom] = {

    chatRoomRepository
      .find(id)
      .map(eChatRoom => {
        val chatMessageSeq = chatMessageRepository
          .findByChatRoom(id)
          .map(message =>
            ChatMessage(message.id, message.chatRoomId, message.message)
          )
        ChatRoom(eChatRoom.id, eChatRoom.name, chatMessageSeq)
      })
  }
  def list: Seq[ChatRoom] = {
    val eChatRoomSeq = chatRoomRepository.list
    val eChatMessageMap =
      chatMessageRepository
        .findByChatRooms(eChatRoomSeq.map(_.id))
        .map(message =>
          ChatMessage(message.id, message.chatRoomId, message.message)
        )
        .groupBy(_.chatRoomId)

    eChatRoomSeq.map(eChatRoom => {
      ChatRoom(
        eChatRoom.id,
        eChatRoom.name,
        eChatMessageMap.getOrElse(eChatRoom.id, Nil)
      )
    })
  }
}
