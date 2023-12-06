package br.edu.scl.ifsp.ads.onemessagechat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.onemessagechat.R
import br.edu.scl.ifsp.ads.onemessagechat.databinding.TileContactBinding
import br.edu.scl.ifsp.ads.onemessagechat.model.Message

class MessageAdapter (context: Context,
                      private val contactList: MutableList<Message>
): ArrayAdapter<Message>(context, R.layout.tile_contact,contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Primeiro passo: pegar o contato
        val contact = contactList[position]
        var tcb: TileContactBinding? = null

        var contactTileView = convertView
        //se for nula, infla a célula
        if(contactTileView == null){
            tcb = TileContactBinding.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            contactTileView = tcb.root

            val tileContactHolder = TileContactHolder(tcb.nameTv,tcb.emailTv)
            contactTileView.tag = tileContactHolder
        }

        (contactTileView.tag as TileContactHolder).nameTv.setText(contact.name)
        (contactTileView.tag as TileContactHolder).emailTv.setText(contact.message)

        //preenche com os dados na célula
        //!! força a chamada porque não precisa verificar se é nulo
        tcb?.nameTv?.setText(contact.name)
        tcb?.emailTv?.setText(contact.message)

        //retorna a célula inflada com os dados
        return contactTileView
    }

    private data class TileContactHolder(val nameTv: TextView, val emailTv: TextView){

    }

}