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
