/*
 * Created by Cba Electronics on 18/9/20 09:41
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    private val mAuth = Firebase.auth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)



        btnResetPassword.setOnClickListener {

            var vProgress = ProgressDialog(this)
            vProgress.setMessage("Aguarde un momento")
            vProgress.show()

            if (emailResetPassword.text.isNullOrBlank()){
                vProgress.dismiss()
                showAlert("Debe completar el email del registro")
            }else{
                mAuth.setLanguageCode("es")
                mAuth.sendPasswordResetEmail(emailResetPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            vProgress.dismiss()
                            showAlert("Se envio un correo para reestablecer su contraseña")

                        }else{
                            vProgress.dismiss()
                            showAlert("No se pudo validar el email ingresado")
                        }
                    }
            }
        }
    }


    private fun showAlert(pMessage: String) {
        Toast.makeText(this, pMessage, Toast.LENGTH_SHORT).show()
    }
}