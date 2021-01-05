/*
 * Created by Cba Electronics on 5/10/20 15:21
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_contacto.*

class ContactoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnEnviarContacto.setOnClickListener {
            val mMessage = txtMensajeComentario.text.toString()
            envarMail(mMessage)
        }

    }

    private fun envarMail(mMessage: String) {
        val mMail = Intent(Intent.ACTION_SEND)
        mMail.data = Uri.parse("mailto:")
        mMail.type = "text/plain"

        mMail.putExtra(Intent.EXTRA_EMAIL, "eduardosanchez77@hotmail.com")
        mMail.putExtra(Intent.EXTRA_SUBJECT, "Mensaje de prueba")
        mMail.putExtra(Intent.EXTRA_TEXT, mMessage)

        startActivity(mMail)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}