package br.edu.scl.ifsp.ads.onemessagechat.model

interface MessageDao {
    fun createContact(contact: Message): Int

    fun retrieveContact(id: Int): Message?

    fun retrieveContacts(): MutableList<Message>

    fun updateContact(contact: Message): Int
}