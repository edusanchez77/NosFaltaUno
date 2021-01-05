/*
 * Created by Cba Electronics on 20/08/20 16:13
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 20/08/20 16:13
 */

package com.cbaelectronics.nosfaltauno.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.dataModel.dataJugar
import kotlinx.android.synthetic.main.content_item_jugar.view.categoriaPartido
import kotlinx.android.synthetic.main.content_item_jugar.view.complejoPartido
import kotlinx.android.synthetic.main.content_item_jugar.view.datePartido
import kotlinx.android.synthetic.main.content_item_jugar.view.generoPartido
import kotlinx.android.synthetic.main.content_item_jugar.view.jugarFotoPerfil
import kotlinx.android.synthetic.main.content_item_jugar.view.vacantesPartido
import kotlinx.android.synthetic.main.content_item_jugar2.view.*
import java.text.SimpleDateFormat

class jugarAdapter(
    private val context: Context,
    private val vFlagNotificaciones: String,
    private val itemClickListener: onPartidoClickListener
): RecyclerView.Adapter<jugarAdapter.ViewHolder>() {

    interface onPartidoClickListener{
        fun onItemClick(
            parPartidoId: String,
            parNombrepartido: String,
            parCategoria: String,
            parFecha: String,
            parGenero: String,
            parVacantes: String,
            parComplejo: String,
            parFotoPerfil: String,
            parNombreUser: String,
            parUserId: String,
            parSolicitudes: Int,
            parTecho: String,
            parPiso: String,
            parPared: String,
            parTokendispositivo: String,
            itemView: View
        )
    }

    private var dataList = mutableListOf<dataJugar>()

    fun setDataList(data: MutableList<dataJugar>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): jugarAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_jugar2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: jugarAdapter.ViewHolder, position: Int) {
        val partidos = dataList[position]
        holder.bindView(partidos)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

        fun bindView(partidos: dataJugar){



            val sdf = SimpleDateFormat("dd/MM HH:mm")
            val date = sdf.format(partidos.par_fechaPartido)

            Glide.with(context).load(partidos.par_fotoPerfil).into(itemView.jugarFotoPerfil)
            //itemView.nombrePartido.text = partidos.par_nombrePartido
            itemView.categoriaPartido.text = partidos.par_categoria
            itemView.generoPartido.text = partidos.par_genero
            itemView.complejoPartido.text = partidos.par_complejo.capitalizeWords()
            itemView.datePartido.text = date
            itemView.notificacionesPartido2.text = partidos.par_solicitudes.toString()

            val color = ContextCompat.getDrawable(context, R.drawable.custom_cardbackground_rojo)
            val colorVerde = ContextCompat.getDrawable(context, R.drawable.custom_cardbackground_verde)

            if(partidos.par_vacantes.toInt() > 0){
                if (partidos.par_vacantes.toInt() == 1){
                    itemView.cardViewPartidos.background = color
                    itemView.vacantesPartido.text = "FALTA "+partidos.par_vacantes
                }else{
                    itemView.vacantesPartido.text = "FALTAN "+partidos.par_vacantes
                }
            }else{
                itemView.vacantesPartido.text = "COMPLETO"
                itemView.cardViewPartidos.background = colorVerde
            }

            if (vFlagNotificaciones == "Y" && partidos.par_solicitudes > 0){
                itemView.notificacionesPartido2.visibility = View.VISIBLE
            }else{
                itemView.notificacionesPartido2.visibility = View.GONE
            }


            itemView.setOnClickListener {
                itemClickListener.onItemClick(
                    partidos.par_partidoId,
                    partidos.par_nombrePartido,
                    partidos.par_categoria,
                    date,
                    partidos.par_genero,
                    partidos.par_vacantes,
                    partidos.par_complejo,
                    partidos.par_fotoPerfil,
                    partidos.par_nombreUser,
                    partidos.par_usrId,
                    partidos.par_solicitudes,
                    partidos.par_techo,
                    partidos.par_piso,
                    partidos.par_pared,
                    partidos.par_tokenDispositivo,
                    itemView
                )
            }
        }
    }
}