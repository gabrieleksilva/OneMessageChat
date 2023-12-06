package br.edu.scl.ifsp.ads.onemessagechat.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.onemessagechat.R
import br.edu.scl.ifsp.ads.onemessagechat.adapter.MessageAdapter
import br.edu.scl.ifsp.ads.onemessagechat.controller.MessageRtDbFbController
import br.edu.scl.ifsp.ads.onemessagechat.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.CONTACT_ARRAY
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.ads.onemessagechat.model.Message

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Lista mutável análogo array Java - Data Source
    private val contactList: MutableList<Message> = mutableListOf()

    //Controller para quem faz as chamadas no BD
    private val contactController: MessageRtDbFbController by lazy {
        MessageRtDbFbController(this)
    }

    private val originalContactList: MutableList<Message> = mutableListOf()

    private val contactAdapter: MessageAdapter by lazy {
        MessageAdapter(
            this,
            contactList)
    }

    companion object {
        const val GET_CONTACTS_MSG = 1
        const val GET_CONTACTS_INTERVAL = 2000L //milissegundos
    }

    //Handler
    val updateContactListHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: android.os.Message){
            super.handleMessage(msg)

            //Busca os contatos ou atualiza a lista de acordo com o tipo da mensagem
            if(msg.what == GET_CONTACTS_MSG){
                //Busca os contatos de acordo com o intervalo pre-definido e agenda nova busca
                contactController.getContacts()
                sendMessageDelayed(obtainMessage().apply { what = GET_CONTACTS_MSG }, GET_CONTACTS_INTERVAL)
            } else{
                msg.data.getParcelableArray(CONTACT_ARRAY)?.also { contactArray ->
                    contactList.clear()
                    contactArray.forEach {
                        contactList.add(it as Message)
                    }
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    private lateinit var searchContactLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root) //A tela vai ser renderizada pela view raiz

        setSupportActionBar(amb.toolbarIn.toolbar)

        amb.contatosLv.adapter = contactAdapter
        //fillContacts()
        originalContactList.addAll(contactList)

        //instancia o carl
        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result -> if(result.resultCode == RESULT_OK) {
            val contact = result.data?.getParcelableExtra<Message>(EXTRA_CONTACT)
            contact?.let{ _contact ->
                if(contactList.any {it.id == _contact.id}){
                    contactController.editContact(_contact)
                }else {
                    contactController.insertContact(_contact)
                }
            }
        }
        }

        searchContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val query = result.data?.getStringExtra("query")
                if (!query.isNullOrBlank()) {
                    performSearch(query)
                }else{
                    Toast.makeText(this, "Valor nulo ou em branco!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        amb.contatosLv.setOnItemClickListener{ parent, view, position, id ->
            // onContextItemSelected(item: MenuItem);
            val contact = contactList[position]
            val editContactIntent = Intent(this, MessageActivity::class.java)
            editContactIntent.putExtra(EXTRA_CONTACT,contact)
            carl.launch(editContactIntent)
        }

        registerForContextMenu(amb.contatosLv)
        updateContactListHandler.apply {
            sendMessageDelayed(
                obtainMessage().apply { what = GET_CONTACTS_MSG },
                GET_CONTACTS_INTERVAL
            )
        }
    }

    private fun performSearch(query: String) {
        val filteredContacts = originalContactList.filter { it.name.contains(query, ignoreCase = true) }
        contactAdapter.clear()
        contactAdapter.addAll(filteredContacts)
        contactAdapter.notifyDataSetChanged()
    }


    //Cria o menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addContactMi -> {
                //Abrir a tela para adicionar um novo contato (ContactActivity)
                carl.launch(Intent(this,MessageActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
//
//        val contact = contactList[position]
//
//        return when(item.itemId){
//            R.id.searchContactMi -> {
//                val searchIntent = Intent(this, SearchActivity::class.java)
//                searchContactLauncher.launch(searchIntent)
//                true
//            }
//            else -> {false}
//        }
//    }

//    private fun clearFilter() {
//        // Restaura a lista de contatos original
//        contactAdapter.clear()
//        contactAdapter.addAll(originalContactList)
//        contactAdapter.notifyDataSetChanged()
//    }
//    private fun fillContacts(){
//        for(i in 1..50){
//            contactList.add(
//                Contact(i,"Nome $i","Endereço $i", "Telefone $i","Email $i")
//            )
//        }
//
//    }

    fun updateContactList(_contactList: MutableList<Message>){
        contactList.clear()
        contactList.addAll(_contactList)
        contactAdapter.notifyDataSetChanged()
    }


}