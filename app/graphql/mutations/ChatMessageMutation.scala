package graphql.mutations

import graphql.models.ChatMessage
import graphql.resolvers.Resolvers
import sangria.schema._

object ChatMessageMutation {
  val mutations: Seq[Field[Resolvers, Unit]] = fields[Resolvers, Unit](
    Field(
      name = "addChatMessage",
      fieldType = ChatMessage.ChatMessageType,
      arguments = List(Argument("chatRoomId", LongType), Argument("message", StringType)),
      resolve = c => c.ctx.chatMessageResolver.add(c.args.arg[Long]("chatRoomId"), c.args.arg[String]("message"))
    )
  )
}
