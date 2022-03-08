/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package graphql.resolvers

import graphql.models.ChatRoom
import repositories.ChatRoomRepository

class ChatRoomResolver {
  // TODO 依存注入
  private val repository: ChatRoomRepository = ChatRoomRepository()

  def find(id: Long): Option[ChatRoom] = repository.find(id).map(eChatRoom => ChatRoom(eChatRoom.id, eChatRoom.name))
  def list: Seq[ChatRoom] = repository.list.map(eChatRoom => ChatRoom(eChatRoom.id, eChatRoom.name))
}
