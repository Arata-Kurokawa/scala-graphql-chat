/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package graphql

import graphql.queries.ChatRoomQuery
import sangria.schema._

object Schema {
  val schema = sangria.schema.Schema[graphql.resolvers.Resolvers, Unit](
    query = ObjectType(
      "Query",
      fields(
        ChatRoomQuery.queries: _*
      )
    )
  )
}
