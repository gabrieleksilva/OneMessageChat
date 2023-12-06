package br.edu.scl.ifsp.ads.onemessagechat.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class MessageDaoRtDbFb: MessageDao {
    companion object {
        private const val CONTACT_LIST_ROOT_NODE = "contactList"
    }

    private val contactRtDbFbReference = Firebase.database.getReference(CONTACT_LIST_ROOT_NODE)

    //Simula uma consulta ao RtDB.
    private val contactList: MutableList<Message> = mutableListOf()

    init{
        contactRtDbFbReference.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Message? = snapshot.getValue<Message>()

                contact?.also { cont ->
                    if (!contactList.any { it.id == cont.id }) {
                        contactList.add(cont)
                    }
                }
            }


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Message? = snapshot.getValue<Message>()

                contact?.also { editedContact ->
                    contactList.apply {
                        this[indexOfFirst { editedContact.id == it.id }] = editedContact
                    }
                }
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {
                val contact: Message? = snapshot.getValue<Message>()

                contact?.also {
                    contactList.remove(it)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //NSA
            }

            override fun onCancelled(error: DatabaseError) {
                //NSA
            }
        })

        //É chamado uma única vez qdo o app é aberto
        contactRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contactMap = snapshot.getValue<Map<String,Message>>()

                contactList.clear()
                contactMap?.values?.also{
                    contactList.addAll(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //NSA
            }
        })
    }

    override fun createContact(contact: Message): Int {
        createOrUpdateContact(contact)
        return 1
    }

    override fun retrieveContact(id: Int): Message? {
        return contactList[contactList.indexOfFirst { it.id == id }]
    }

    override fun retrieveContacts(): MutableList<Message> = contactList

    override fun updateContact(contact: Message): Int {
        createOrUpdateContact(contact)
        return 1
    }


    private fun createOrUpdateContact(contact:Message) = contactRtDbFbReference.child(contact.id.toString()).setValue(contact)
}