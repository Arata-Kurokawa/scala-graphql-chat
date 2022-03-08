/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package repositories

import entities.ChatRoom

case class ChatRoomRepository () {
  private val chatRooms = Seq(
    ChatRoom(1L, "chat room 1"),
    ChatRoom(2L, "chat room 2"),
    ChatRoom(3L, "chat room 3"),
    ChatRoom(4L, "chat room 4")
  )

  def find(id: Long): Option[ChatRoom] = chatRooms.find(_.id == id)
  def list: Seq[ChatRoom] = chatRooms
}
