/*
 * Created by Cba Electronics on 03/09/20 14:58
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 03/09/20 14:58
 */

package com.cbaelectronics.nosfaltauno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.nosfaltauno.adapters.notificacionesAdapter
import com.cbaelectronics.nosfaltauno.adapters.vacantesAdapter
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import kotlinx.android.synthetic.main.activity_detalle_mis_partidos.*
import kotlinx.android.synthetic.main.activity_detalle_partidos.detalleCategoria
import kotlinx.android.synthetic.main.activity_detalle_partidos.detalleComplejo
import kotlinx.android.synthetic.main.activity_detalle_partidos.detalleFecha
import kotlinx.android.synthetic.main.activity_detalle_partidos.detalleGenero

class DetalleMisPartidosActivity : AppCompatActivity(), notificacionesAdapter.onSolicitudClickListener {

    private lateinit var adapter: notificacionesAdapter
    private lateinit var adapterVacantes: vacantesAdapter
    val viewModel: DetallePartidosViewModel by lazy { ViewModelProviders.of(this).get(DetallePartidosViewModel::class.java) }

    private val key             = "MY_KEY"
    private val usrId           = "MY_ID"
    private val usrNombre       = "MY_NOMBRE"
    private val usrEmail        = "MY_EMAIL"
    private val usrFotoPerfil   = "MY_FOTOPERFIL"

    val setFirebase =
        setFirestore(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_mis_partidos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

        detalleNombreMiPartido.text = intent.extras?.getString("nombrePartido")?.capitalizeWords()
        //detalleVacantes.text = intent.extras?.getString("vacantes")
        detalleCategoria.text = intent.extras?.getString("categoria")
        detalleComplejo.text = intent.extras?.getString("complejo")?.capitalizeWords()
        detalleGenero.text = intent.extras?.getString("genero")
        detalleFecha.text = intent.extras?.getString("fecha")

        if (prefs.getString(usrId, "DEFAULT").toString() != intent.extras?.getString("userId")){
            tituloSolicituces.visibility = View.GONE
        }

        adapterVacantes = vacantesAdapter(this)
        recyclerViewVacantes.layoutManager = LinearLayoutManager(this)
        recyclerViewVacantes.adapter = adapterVacantes
        observeVacantes(intent.extras?.getString("partidoId").toString())


        adapter = notificacionesAdapter(this, this)
        recyclerViewNotificaciones.layoutManager = LinearLayoutManager(this)
        recyclerViewNotificaciones.adapter = adapter

        observeData(intent.extras?.getString("partidoId").toString(),
            prefs.getString(usrId, "DEFAULT").toString()
        )
    }

    private fun observeVacantes(pPartidoId: String) {
        viewModel.fetchVacantesData(pPartidoId).observe(this, Observer {
            if (it.size > 0){
                txtNoHaySolicitudes.visibility = View.GONE
            }else{
                txtNoHaySolicitudes.visibility = View.VISIBLE
            }
            adapterVacantes.setDataList(it)
            adapterVacantes.notifyDataSetChanged()
        })
    }

    private fun observeData(pPartidoId: String, pUserId: String) {

        viewModel.fetchNotificacionesData(pPartidoId, pUserId).observe(this, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun aceptarSolicitud(
        notifUserNombre: String,
        notifUserFotoPerfil: String,
        notificacionId: String,
        notifPartidoId: String,
        notifVacantes: Int,
        notifUserId: String
    ) {
        setFirebase.changeStatusSolicitud(notificacionId, "Y", notifPartidoId, notifUserNombre, notifUserFotoPerfil, notifVacantes, notifUserId)
    }

    override fun rechazarSolicitud(
        notifUserNombre: String,
        notifUserFotoPerfil: String,
        notificacionId: String,
        notifPartidoId: String,
        notifVacantes: Int,
        notifUserId: String
    ) {
        setFirebase.changeStatusSolicitud(
            notificacionId,
            "N",
            notifPartidoId,
            notifUserNombre,
            notifUserFotoPerfil,
            notifVacantes,
            notifUserId
        )
    }
}