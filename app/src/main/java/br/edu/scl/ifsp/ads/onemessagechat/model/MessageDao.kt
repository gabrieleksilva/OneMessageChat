package br.edu.scl.ifsp.ads.onemessagechat.model

interface MessageDao {
    fun createContact(contact: Contact): Int

    fun retrieveContact(id: Int): Contact?

    fun retrieveContacts(): MutableList<Contact>

    fun updateContact(contact: Contact): Int
}