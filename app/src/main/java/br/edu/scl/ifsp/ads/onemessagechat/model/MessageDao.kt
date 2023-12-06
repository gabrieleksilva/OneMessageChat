package br.edu.scl.ifsp.ads.onemessagechat.model

interface MessageDao {
    fun createContact(message: Message): Int

    fun retrieveContact(id: Int): Message?

    fun retrieveContacts(): MutableList<Message>

    fun updateContact(contact: Message): Int
}