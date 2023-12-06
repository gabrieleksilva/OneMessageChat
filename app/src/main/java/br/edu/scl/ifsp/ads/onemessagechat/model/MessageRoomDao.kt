package br.edu.scl.ifsp.ads.onemessagechat.model

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface MessageRoomDao {

    companion object {
        const val MESSAGE_DATABASE_FILE =
            "contacts_room" //cria o arquivo de BD dentro da área de armazenamento do app
        private const val CONTACT_TABLE = "message"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val MESSAGE_COLUMN = "message"
    }

    @Insert
    fun createContact(contact: Message)

    @Query("SELECT * FROM $CONTACT_TABLE WHERE $ID_COLUMN = :id")
    fun retrieveContact(id: Int): Message?

    @Query("SELECT * FROM $CONTACT_TABLE ORDER BY $NAME_COLUMN")
    fun retrieveContacts(): MutableList<Message>

    @Update
    fun updateContact(contact: Message): Int

    @Delete
    fun deleteContact(contact: Message): Int
}