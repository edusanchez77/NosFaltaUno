/*
 * Created by Cba Electronics on 14/09/20 12:52
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 14/09/20 12:52
 */

package com.cbaelectronics.nosfaltauno

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.DialogPreference
import android.preference.PreferenceManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_complete_register.*

class CompleteRegisterActivity : AppCompatActivity() {

    private val key             = "MY_KEY"
    private val usrId           = "MY_ID"
    private val usrNombre       = "MY_NOMBRE"
    private val usrEmail        = "MY_EMAIL"
    private val usrFotoPerfil   = "MY_FOTOPERFIL"
    private val usrCategory     = "MY_CATEGORY"
    private val usrPosition     = "MY_POSITION"

    val setFirebase =
        setFirestore(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_register)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)


        val arrayCategory = resources.getStringArray(R.array.strCategory)
        val arrayPosition = resources.getStringArray(R.array.strPosition)

        val adapterCategory = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayCategory
        )

        val adapterPosition = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayPosition
        )

        spinnerCategoryModifPerfil.setAdapter(adapterCategory)
        spinnerPositionModifPerfil.setAdapter(adapterPosition)

        val user = Firebase.auth.currentUser

        btnGuardarPerfilCompleto.setOnClickListener {
            if (spinnerCategoryModifPerfil.text.toString().isNullOrBlank() ||
                spinnerPositionModifPerfil.text.toString().isNullOrBlank()){
                val dialog = AlertDialog.Builder(this)
                dialog.setMessage("Tenes que completar todos los datos")
                dialog.setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    })
                dialog.show()
            }else{
                updatePerfil(
                    user?.uid.toString(),
                    spinnerCategoryModifPerfil.text.toString(),
                    spinnerPositionModifPerfil.text.toString()
                )
                val homeActivity = Intent(this, HomeActivity::class.java)
                showActivity(homeActivity)
            }

        }

    }

    private fun showActivity(activity: Intent) {

        startActivity(activity)

    }

    private fun updatePerfil(usrId: String, category: String, position: String) {

        setFirebase.updatePerfil(usrId, category, position)

    }
}