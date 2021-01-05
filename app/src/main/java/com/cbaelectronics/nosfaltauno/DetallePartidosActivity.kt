/*
 * Created by Cba Electronics on 21/08/20 17:53
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 21/08/20 17:53
 */

package com.cbaelectronics.nosfaltauno

import android.content.DialogInterface
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.dataModel.firebase.getFirestore
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detalle_partidos.*

class DetallePartidosActivity : AppCompatActivity() {

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_partidos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val setFirebase =
            setFirestore(this)

        val getFirestore = getFirestore()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        FirebaseFirestore.getInstance().collection("notificaciones")
            .whereEqualTo("not_partidoId", intent.extras?.getString("partidoId").toString())
            .whereEqualTo("datosUser.usr_id", prefs.getString(usrId, "DEFAULT").toString())
            //.whereEqualTo("not_status", "P")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    when(document.get("not_status")){
                        "P" -> {
                            btnPostularme.isEnabled = false
                            btnPostularme.text = "PENDIENTE DE ACEPTACION"
                        }
                        "Y" -> {
                            btnPostularme.isEnabled = false
                            btnPostularme.text = "YA FUISTE ACEPTADO PARA JUGAR ESTE PARTIDO"
                        }
                        "N" -> {
                            btnPostularme.isEnabled = false
                            btnPostularme.text = "NO FUISTE ACEPTADO PARA JUGAR"
                        }
                    }
                }
            }


        /*val vPostulate = getFirestore.getPostulation(
            prefs.getString(usrId, "DEFAULT").toString(),
            intent.extras?.getString("partidoId").toString()
        )*/




        fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

        if (prefs.getString(usrId, "DEFAULT").toString() == intent.extras?.getString("userId")){
            partidoPropio.visibility = View.VISIBLE
            btnPostularme.visibility = View.INVISIBLE
        }else{
            partidoPropio.visibility = View.GONE
        }

        var vVacantes = "COMPLETO"
        if(intent.extras?.getString("vacantes")?.toInt()!! > 0){
            vVacantes = "FALTAN\n" + intent.extras?.getString("vacantes")
        }else{
            btnPostularme.text = "YA NO HAY VACANTES"
            btnPostularme.isEnabled = false
        }


        Glide.with(this).load(intent.extras?.getString("fotoPerfil")).into(detalleFotoPerfil)
        detalleNombreUser.text = intent.extras?.getString("nombreUsuario")
        detalleNombrePartido.text = intent.extras?.getString("nombrePartido")
        detalleVacantes.text = vVacantes
        detalleCategoria.text = intent.extras?.getString("categoria")
        detalleComplejo.text = intent.extras?.getString("complejo")?.capitalizeWords()
        detalleGenero.text = intent.extras?.getString("genero")
        detalleFecha.text = intent.extras?.getString("fecha")
        detalleTecho.text = intent.extras?.getString("techo")
        detallePiso.text = intent.extras?.getString("piso")
        detallePared.text = intent.extras?.getString("pared")

        when(intent.extras?.getString("vacantes")?.toInt()){
            0 -> {
                imagenJugador1.setImageResource(R.drawable.ic_ocupado)
                imagenJugador2.setImageResource(R.drawable.ic_ocupado)
                imagenJugador3.setImageResource(R.drawable.ic_ocupado)
                imagenJugador4.setImageResource(R.drawable.ic_ocupado)
                textoJugador1.text = "OCUPADO"
                textoJugador2.text = "OCUPADO"
                textoJugador3.text = "OCUPADO"
                textoJugador4.text = "OCUPADO"
            }
            1 -> {
                imagenJugador1.setImageResource(R.drawable.ic_ocupado)
                imagenJugador2.setImageResource(R.drawable.ic_ocupado)
                imagenJugador3.setImageResource(R.drawable.ic_ocupado)
                imagenJugador4.setImageResource(R.drawable.ic_libre)
                textoJugador1.text = "OCUPADO"
                textoJugador2.text = "OCUPADO"
                textoJugador3.text = "OCUPADO"
                textoJugador4.text = "LIBRE"
            }
            2 -> {
                imagenJugador1.setImageResource(R.drawable.ic_ocupado)
                imagenJugador2.setImageResource(R.drawable.ic_ocupado)
                imagenJugador3.setImageResource(R.drawable.ic_libre)
                imagenJugador4.setImageResource(R.drawable.ic_libre)
                textoJugador1.text = "OCUPADO"
                textoJugador2.text = "OCUPADO"
                textoJugador3.text = "LIBRE"
                textoJugador4.text = "LIBRE"
            }
            3 -> {
                imagenJugador1.setImageResource(R.drawable.ic_ocupado)
                imagenJugador2.setImageResource(R.drawable.ic_libre)
                imagenJugador3.setImageResource(R.drawable.ic_libre)
                imagenJugador4.setImageResource(R.drawable.ic_libre)
                textoJugador1.text = "OCUPADO"
                textoJugador2.text = "LIBRE"
                textoJugador3.text = "LIBRE"
                textoJugador4.text = "LIBRE"
            }
            4 -> {
                imagenJugador1.setImageResource(R.drawable.ic_libre)
                imagenJugador2.setImageResource(R.drawable.ic_libre)
                imagenJugador3.setImageResource(R.drawable.ic_libre)
                imagenJugador4.setImageResource(R.drawable.ic_libre)
                textoJugador1.text = "LIBRE"
                textoJugador2.text = "LIBRE"
                textoJugador3.text = "LIBRE"
                textoJugador4.text = "LIBRE"
            }
        }

        btnPostularme.setOnClickListener {
            val vAlert = AlertDialog.Builder(this)
            vAlert.setMessage("¿Querés postularte para jugar este partido?")
            vAlert.setCancelable(false)
            vAlert.setNegativeButton("NO", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
            vAlert.setPositiveButton("SI", DialogInterface.OnClickListener { dialogInterface, i ->
                setFirebase.setSendPostulacion(
                    intent.extras?.getString("partidoId").toString(),
                    intent.extras?.getString("userId").toString(),
                    intent.extras?.getString("tokenDispositivo").toString(),
                    prefs.getString(usrId, "DEFAULT").toString(),
                    prefs.getString(usrNombre, "DEFAULT").toString(),
                    prefs.getString(usrFotoPerfil, "DEFAULT").toString(),
                    prefs.getString(usrTokenDispositivo, "DEFAULT").toString(),
                    prefs.getString(usrPosition, "DEFAULT").toString(),
                    intent.extras!!.getInt("solicitudes")+1,
                    intent.extras?.getString("vacantes")
                )

                btnPostularme.text = "PENDIENTE DE ACEPTACIÓN"
                btnPostularme.isEnabled = false
                btnPostularme.background = resources.getDrawable(R.color.md_green_300)
            })
            vAlert.show()
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}