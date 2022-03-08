/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package graphql.models

import sangria.macros.derive._

import graphql.models.LongIdentifiable._

case class ChatRoom(id: Long, name: String) extends LongIdentifiable

object ChatRoom {
  val ChatRoomType = deriveObjectType[Unit, ChatRoom](Interfaces(LongIdentifiableType))
}
