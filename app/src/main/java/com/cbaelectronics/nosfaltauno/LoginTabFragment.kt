/*
 * Created by Cba Electronics on 2/10/20 16:32
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_tab_fragment.*
import kotlinx.android.synthetic.main.login_tab_fragment.view.*

class LoginTabFragment: Fragment() {

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.login_tab_fragment, container, false)
        val prefs = PreferenceManager.getDefaultSharedPreferences(root.context)


        //Olvide mi password
        root.btnOlvidePassword.setOnClickListener {
            startActivity(Intent(root.context, ResetPasswordActivity::class.java))
        }


        //Inicio de sesion con Email y Password
        root.btnLogin.setOnClickListener {
            val vProgress = ProgressDialog(root.context)
            if(emailLogin.text.toString().isNotEmpty() && passLogin.text.toString().isNotEmpty()){

                vProgress.setMessage("Iniciando sesión")
                vProgress.show()

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        emailLogin.text.toString(),
                        passLogin.text.toString()
                    )
                    .addOnCompleteListener(){
                        if (it.isSuccessful){

                            val editor = prefs.edit()
                            editor.putString(key, "Y")
                            editor.apply()

                            vProgress.dismiss()
                            showHome(root.context)
                        }else{
                            vProgress.dismiss()
                            val mMessage = "Se ha producido un error autenticando al usuario"
                            showAlert(mMessage, root.context)
                        }
                    }
            }else{
                val mMessage = "Se ha producido un error autenticando al usuario"
                showAlert(mMessage, root.context)
            }
        }

        return root
    }

    private fun showHome(context: Context) {
        val homeIntent = Intent(context, HomeActivity::class.java)
        startActivity(homeIntent)
        //finish()
    }

    private fun showAlert(mMessage: String, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(mMessage)
        builder.setPositiveButton("Aceptar", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}