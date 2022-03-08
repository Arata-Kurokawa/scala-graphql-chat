/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package repositories

import entities.ChatMessage

case class ChatMessageRepository() {
  private val chatMessages = Seq(
    ChatMessage(1L, 1L, "chat room 1 message 1"),
    ChatMessage(2L, 1L, "chat room 1 message 2"),
    ChatMessage(3L, 2L, "chat room 2 message 3"),
    ChatMessage(4L, 3L, "chat room 3 message 4"),
    ChatMessage(5L, 4L, "chat room 4 message 5"),
    ChatMessage(6L, 4L, "chat room 4 message 6")
  )

  def findByChatRoom(chatRoomId: Long): Seq[ChatMessage] = {
    chatMessages.filter(_.chatRoomId == chatRoomId)
  }

  def findByChatRooms(chatRoomIdSeq: Seq[Long]): Seq[ChatMessage] = {
    chatMessages.filter(message => chatRoomIdSeq.contains(message.chatRoomId))
  }
}
