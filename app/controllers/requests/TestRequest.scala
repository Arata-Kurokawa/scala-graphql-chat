package controllers.requests

import sangria.macros._

object TestRequest {
  val chatRoomQuery1 =
    graphql"""
      query chatRoom1 {
        chatRoom(id: 1) {
          name
          messages {
            id
            message
          }
        }

        chatRooms {
          name
        }
      }
    """

  val chatRoomQuery2 =
    graphql"""
      query chatRoom2 {
        chatRoom(id: 2) {
          id
          name
          messages {
            id
            message
          }
        }

        chatRooms {
          id
          name
          messages {
            id
            message
          }
        }
      }
    """
}
