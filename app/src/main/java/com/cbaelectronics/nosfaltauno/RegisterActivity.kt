package com.cbaelectronics.nosfaltauno

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        spinnerCategoryRegistro.setAdapter(adapterCategory)
        spinnerPositionRegistro.setAdapter(adapterPosition)

        val setFirebase =
            setFirestore(this)


        btnRegistrar.setOnClickListener {

            val vProgress = ProgressDialog(this)

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
                                intent.extras?.getString("tokenDispositivo").toString()
                            )

                            vProgress.dismiss()
                            showHome(it.result?.user?.email ?: "")
                            finish()
                        }else{
                            vProgress.dismiss()
                            showAlert("Se ha producido un error autenticando al usuario.\nRevise la dirección de correo.")
                        }
                    }

            }else{
                showAlert("Debe completar todos los datos para registrarse")
            }
        }


    }

    private fun showHome(pEmail: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", pEmail)
        }
        startActivity(homeIntent)
    }

    private fun showAlert(pMensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(pMensaje)
        builder.setPositiveButton("Aceptar", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}