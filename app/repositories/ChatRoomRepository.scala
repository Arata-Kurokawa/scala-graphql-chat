package repositories

import entities.ChatRoom
import repositories.persistences.mysql.ChatRoomTable
import scalikejdbc._

case class ChatRoomRepository() {
  def find(id: Long): Option[ChatRoom] = {
    val cr = ChatRoomTable.syntax("cr")

    DB.localTx { implicit session =>
      withSQL {
        select.from(ChatRoomTable as cr).where.eq(cr.id, id)
      }.map(ChatRoomTable(cr.resultName)).single().apply()
    }
  }

  def list: Seq[ChatRoom] = {
    val cr = ChatRoomTable.syntax("cr")

    DB.localTx { implicit session =>
      withSQL {
        select.from(ChatRoomTable as cr)
      }.map(ChatRoomTable(cr.resultName)).list().apply()
    }
  }
}
