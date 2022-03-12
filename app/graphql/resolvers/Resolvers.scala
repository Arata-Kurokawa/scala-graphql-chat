package graphql.resolvers

case class Resolvers() {
  val chatRoomResolver: ChatRoomResolver = ChatRoomResolver()
  val chatMessageResolver: ChatMessageResolver = ChatMessageResolver()
}
