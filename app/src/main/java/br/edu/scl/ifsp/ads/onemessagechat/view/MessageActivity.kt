package br.edu.scl.ifsp.ads.onemessagechat.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.onemessagechat.databinding.ActivityMessageBinding
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.VIEW_CONTACT
import br.edu.scl.ifsp.ads.onemessagechat.model.Message

class MessageActivity : AppCompatActivity() {

    private val acb: ActivityMessageBinding by lazy {
        ActivityMessageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Contact details"

        val receivedContact = intent.getParcelableExtra<Message>(EXTRA_CONTACT)
        receivedContact?.let {_receivedContact ->
            with(acb){
                nameEt.setText(_receivedContact.name)
                messageEt.setText(_receivedContact.message)
            }
            val viewContact = intent.getBooleanExtra(VIEW_CONTACT, false)
            if (viewContact){
                with(acb){
                    nameEt.isEnabled = false
                    messageEt.isEnabled = false
                    saveBt.visibility= View.GONE
                }
            }

        }

        with(acb){
            saveBt.setOnClickListener {
                val contact: Message = Message(
                    id = receivedContact?.id,
                    nameEt.text.toString(),
                    messageEt.text.toString(),
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_CONTACT,contact)
                setResult(RESULT_OK,resultIntent)
                finish()

            }

        }
    }
}