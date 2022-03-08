/*
 * This file is part of the KIDSNA Connect service.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers.requests

import sangria.macros._

object TestRequest {
  val chatRoomQuery1 =
    graphql"""
      query chatRoom1 {
        chatRoom(id: 1) {
          name
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
        }

        chatRooms {
          id
          name
        }
      }
    """
}
