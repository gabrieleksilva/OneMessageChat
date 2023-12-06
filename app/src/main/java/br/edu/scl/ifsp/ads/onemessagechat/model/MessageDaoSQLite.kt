package br.edu.scl.ifsp.ads.onemessagechat.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.scl.ifsp.ads.onemessagechat.R
import java.sql.SQLException

class MessageDaoSlite (context: Context): MessageDao {
    //Criação de um singleton para armazenar as colunas das tabelas
    companion object Constant {
        private const val CONTACT_DATABASE_FILE = "contacts" //cria o arquivo de BD dentro da área de armazenamento do app
        private const val CONTACT_TABLE = "contact"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val MESSAGE_COLUMN = "message"

        private const val CREATE_CONTACT_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $CONTACT_TABLE ("+
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$MESSAGE_COLUMN TEXT NOT NULL);"
    }

    //referência pro BD
    private val contactsSqliteDatabase: SQLiteDatabase
    init{
        contactsSqliteDatabase =
            context.openOrCreateDatabase(CONTACT_DATABASE_FILE, MODE_PRIVATE, null)
        try {
            contactsSqliteDatabase.execSQL(CREATE_CONTACT_TABLE_STATEMENT)
        }
        catch (se: SQLException){
            Log.e(context.getString(R.string.app_name),se.message.toString())
        }
    }


    override fun createContact(contact: Message) = contactsSqliteDatabase.insert(CONTACT_TABLE, null, contact.toContentValues()).toInt()

    override fun retrieveContact(id: Int): Message? {
        val cursor = contactsSqliteDatabase.rawQuery("SELECT * FROM $CONTACT_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )
        val contact = if(cursor.moveToFirst()) cursor.rowToContact() else null
        cursor.close()
        return contact
    }

    override fun retrieveContacts(): MutableList<Message> {
        val contactList = mutableListOf<Message>()

        val cursor = contactsSqliteDatabase.rawQuery("SELECT * FROM $CONTACT_TABLE ORDER BY $NAME_COLUMN",null)
        while(cursor.moveToNext()){
            contactList.add(cursor.rowToContact())
        }
        cursor.close()
        return contactList
    }

    override fun updateContact(contact: Message) = contactsSqliteDatabase.update(
        CONTACT_TABLE,
        contact.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(contact.id.toString())
        )


    private fun Cursor.rowToContact() = Message(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(NAME_COLUMN)),
        getString(getColumnIndexOrThrow(MESSAGE_COLUMN))
    )


    private fun Message.toContentValues() = with (ContentValues()){
        put(NAME_COLUMN, name)
        put(MESSAGE_COLUMN, message)
        this
    }


}