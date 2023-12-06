package br.edu.scl.ifsp.ads.onemessagechat.controller

import br.edu.scl.ifsp.ads.onemessagechat.model.Constant
import br.edu.scl.ifsp.ads.onemessagechat.model.Message
import br.edu.scl.ifsp.ads.onemessagechat.model.MessageDao
import br.edu.scl.ifsp.ads.onemessagechat.model.MessageDaoRtDbFb
import br.edu.scl.ifsp.ads.onemessagechat.view.MainActivity

class MessageRtDbFbController (private val mainActivity: MainActivity) {
    private val contactDaoImpl: MessageDao = MessageDaoRtDbFb()

    fun insertContact(contact: Message) {
        Thread {
            contactDaoImpl.createContact(contact)
        }.start()
    }

    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)

    fun getContacts() {
        Thread{
            mainActivity.updateContactListHandler.apply {
                sendMessage(
                    obtainMessage().apply {
                        data.putParcelableArray(
                            Constant.CONTACT_ARRAY,
                            contactDaoImpl.retrieveContacts().toTypedArray()
                        )
                    }
                )
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