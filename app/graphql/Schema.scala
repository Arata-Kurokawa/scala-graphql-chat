package graphql

import graphql.mutations.ChatMessageMutation
import graphql.queries.ChatRoomQuery
import sangria.schema._

object Schema {
  val schema = sangria.schema.Schema[graphql.resolvers.Resolvers, Unit](
    query = ObjectType(
      "Query",
      fields(
        ChatRoomQuery.queries: _*
      )
    ),
    mutation = Some(
      ObjectType(
        "Mutation",
        fields(
          ChatMessageMutation.mutations: _*
        )
      )
    )
  )
}
