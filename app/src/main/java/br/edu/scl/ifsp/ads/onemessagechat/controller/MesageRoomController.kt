package br.edu.scl.ifsp.ads.onemessagechat.controller

import androidx.room.Room
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.CONTACT_ARRAY
import br.edu.scl.ifsp.ads.onemessagechat.model.Message
import br.edu.scl.ifsp.ads.onemessagechat.model.MessageRoomDao
import br.edu.scl.ifsp.ads.onemessagechat.model.MessageRoomDao.Companion.MESSAGE_DATABASE_FILE
import br.edu.scl.ifsp.ads.onemessagechat.model.MessageRoomDaoDatabase
import br.edu.scl.ifsp.ads.onemessagechat.view.MainActivity

class MesageRoomController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: MessageRoomDao by lazy {
        Room.databaseBuilder(
            mainActivity,
            MessageRoomDaoDatabase::class.java,
            MESSAGE_DATABASE_FILE
        ).build().getContactRoomDao()
    }

    fun insertContact(contact: Message) {
        Thread {
            contactDaoImpl.createContact(contact)
            getContacts()
        }.start()
    }

    fun getContacts() {
        Thread{
            mainActivity.updateContactListHandler.apply {
                sendMessage(
                    obtainMessage().apply {
                    data.putParcelableArray(
                        CONTACT_ARRAY,
                        contactDaoImpl.retrieveContacts().toTypedArray()
                    )
                })
            }
        }.start()
    }

    fun editContact(contact: Message){
        Thread {
            contactDaoImpl.updateContact(contact)
            getContacts()
        }.start()
    }

}