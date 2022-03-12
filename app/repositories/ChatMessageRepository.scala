package repositories

import entities.ChatMessage
import repositories.persistences.mysql.ChatMessageTable
import scalikejdbc._

case class ChatMessageRepository() {
  def findByChatRoom(chatRoomId: Long): Seq[ChatMessage] = {
    val cm = ChatMessageTable.syntax("cm")

    DB.localTx { implicit session =>
      withSQL {
        select.from(ChatMessageTable as cm).where.eq(cm.chatRoomId, chatRoomId)
      }.map(ChatMessageTable(cm.resultName)).list().apply()
    }
  }

  def findByChatRooms(chatRoomIdSeq: Seq[Long]): Seq[ChatMessage] = {
    val cm = ChatMessageTable.syntax("cm")

    DB.localTx { implicit session =>
      withSQL {
        select.from(ChatMessageTable as cm).where.in(cm.chatRoomId, chatRoomIdSeq)
      }.map(ChatMessageTable(cm.resultName)).list().apply()
    }
  }

  def add(chatRoomId: Long, message: String): Long = {
    DB.localTx { implicit session =>
      withSQL {
        insert.into(ChatMessageTable).namedValues(
          ChatMessageTable.column.chatRoomId -> chatRoomId,
          ChatMessageTable.column.message -> message
        )
      }.updateAndReturnGeneratedKey().apply()
    }
  }
}
