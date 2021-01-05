/*
 * Created by Cba Electronics on 2/10/20 16:32
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.signup_tab_fragment.view.*

class SignupTabFragment: Fragment() {

    private var tokenDispositivo = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.signup_tab_fragment, container, false)
        val arrayCategory = resources.getStringArray(R.array.strCategory)
        val arrayPosition = resources.getStringArray(R.array.strPosition)
        val setFirebase = setFirestore(root.context)

        val adapterCategory = ArrayAdapter(
            root.context,
            R.layout.dropdown_menu_popup_item,
            arrayCategory
        )

        val adapterPosition = ArrayAdapter(
            root.context,
            R.layout.dropdown_menu_popup_item,
            arrayPosition
        )

        //Obtengo el token del dispositivo para enviar las notificaciones
        tokenDispositivo = getTokenDispositivo()

        root.spinnerCategoryRegistro.setAdapter(adapterCategory)
        root.spinnerPositionRegistro.setAdapter(adapterPosition)


        root.btnRegistrar.setOnClickListener {

            val vProgress = ProgressDialog(root.context)

            if( nombreRegistro.text.toString().isNotEmpty() &&
                apellidoRegistro.text.toString().isNotEmpty() &&
                emailRegistro.text.toString().isNotEmpty() &&
                passRegistro.text.toString().isNotEmpty() &&
                spinnerCategoryRegistro.text.toString().isNotEmpty() &&
                spinnerPositionRegistro.text.toString().isNotEmpty()){

                vProgress.setMessage("Registrando usuario")
                vProgress.show()

                var vAuth = Firebase.auth

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        emailRegistro.text.toString(),
                        passRegistro.text.toString()
                    )
                    .addOnCompleteListener(){
                        if (it.isSuccessful){
                            val datosUser = vAuth.currentUser

                            setFirebase.setUser(
                                datosUser?.uid.toString(),
                                nombreRegistro.text.toString(),
                                apellidoRegistro.text.toString(),
                                emailRegistro.text.toString(),
                                "https://firebasestorage.googleapis.com/v0/b/nos-falta-uno-d5f3c.appspot.com/o/fotoPerfil%2FperfilDefault.jpg?alt=media&token=54fdcc45-18e0-4918-b5fc-15ce66ce4cbc",
                                spinnerCategoryRegistro.text.toString(),
                                spinnerPositionRegistro.text.toString(),
                                "MAIL",
                                tokenDispositivo
                            )

                            vProgress.dismiss()
                            showHome(it.result?.user?.email ?: "", root.context)
                        }else{
                            vProgress.dismiss()
                            showAlert("Se ha producido un error autenticando al usuario.\nRevise la dirección de correo.", root.context)
                        }
                    }

            }else{
                showAlert("Debe completar todos los datos para registrarse", root.context)
            }
        }


        return root
    }


    private fun showHome(pEmail: String, context: Context) {
        val homeIntent = Intent(context, HomeActivity::class.java).apply {
            putExtra("email", pEmail)
        }
        startActivity(homeIntent)
    }

    private fun showAlert(pMensaje: String, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(pMensaje)
        builder.setPositiveButton("Aceptar", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun getTokenDispositivo(): String {

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            it.result?.token?.let {
                tokenDispositivo = it
            }
        }

        return tokenDispositivo
    }

}