package graphql.queries

import graphql.resolvers.Resolvers
import sangria.schema._

import graphql.models.ChatRoom._

object ChatRoomQuery {
  val queries: Seq[Field[Resolvers, Unit]] = fields[Resolvers, Unit](
    Field(
      name = "chatRoom",
      fieldType = OptionType(ChatRoomType),
      arguments = List(Argument("id", LongType)),
      resolve = c => c.ctx.chatRoomResolver.find(c.args.arg[Long]("id"))
    ),
    Field(
      name = "chatRooms",
      fieldType = ListType(ChatRoomType),
      resolve = c => c.ctx.chatRoomResolver.list
    )
  )
}
