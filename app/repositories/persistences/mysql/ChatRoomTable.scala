/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package repositories.persistences.mysql

import entities.ChatRoom
import scalikejdbc._

import java.time.ZonedDateTime

case class ChatRoomDataModel(
    id: Long,
    name: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
)
object ChatRoomTable extends SQLSyntaxSupport[ChatRoomDataModel] {
  override val tableName = "chat_rooms"

  def apply(
      m: ResultName[ChatRoomDataModel]
  )(rs: WrappedResultSet): ChatRoom = {
    ChatRoom(
      id = rs.long(m.id),
      name = rs.string(m.name),
      createdAt = rs.zonedDateTime(m.createdAt),
      updatedAt = rs.zonedDateTime(m.updatedAt)
    )
  }
}
