/*
 * Created by Cba Electronics on 02/09/20 16:04
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 02/09/20 16:04
 */

package com.cbaelectronics.nosfaltauno.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.dataModel.dataNotificacion
import kotlinx.android.synthetic.main.content_item_notificaciones.view.*

class notificacionesAdapter(
    private val context: Context,
    private val itemClickListener: notificacionesAdapter.onSolicitudClickListener
): RecyclerView.Adapter<notificacionesAdapter.ViewHolder>() {

    interface onSolicitudClickListener{
        fun aceptarSolicitud(
            notifUserNombre: String,
            notifUserFotoPerfil: String,
            notificacionId: String,
            notifPartidoId: String,
            notifVacantes: Int,
            notifUserId: String
        )

        fun rechazarSolicitud(
            notifUserNombre: String,
            notifUserFotoPerfil: String,
            notificacionId: String,
            notifPartidoId: String,
            notifVacantes: Int,
            notifUserId: String
        )
    }

    private var dataList = mutableListOf<dataNotificacion>()

    fun setDataList(data: MutableList<dataNotificacion>){
        dataList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): notificacionesAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_notificaciones, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: notificacionesAdapter.ViewHolder, position: Int) {
        val vNotificacion = dataList[position]
        holder.bindView(vNotificacion)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(notificacion: dataNotificacion){

            Glide.with(context).load(notificacion.notifUserFotoPerfil).into(itemView.notificacionFotoPerfil)
            itemView.notificacionNombreUser.text = notificacion.notifUserNombre
            itemView.notificacionPosicionUser.text = notificacion.notifUserPosicion

            itemView.btnAceptarSolicitud.setOnClickListener {
                itemClickListener.aceptarSolicitud(
                    notificacion.notifUserNombre,
                    notificacion.notifUserFotoPerfil,
                    notificacion.notificacionId,
                    notificacion.notifPartidoId,
                    notificacion.notifVacantes,
                    notificacion.notifUserId
                )
            }

            itemView.btnRechazarSolicitud.setOnClickListener {
                itemClickListener.rechazarSolicitud(
                    notificacion.notifUserNombre,
                    notificacion.notifUserFotoPerfil,
                    notificacion.notificacionId,
                    notificacion.notifPartidoId,
                    notificacion.notifVacantes,
                    notificacion.notifUserId
                )
            }
        }
    }
}