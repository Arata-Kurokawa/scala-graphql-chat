/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package graphql.models

import graphql.models.LongIdentifiable.LongIdentifiableType
import sangria.macros.derive._

case class ChatMessage(id: Long, chatRoomId: Long, message: String)
    extends LongIdentifiable

object ChatMessage {
  implicit val ChatMessageType =
    deriveObjectType[Unit, ChatMessage](Interfaces(LongIdentifiableType))
}
