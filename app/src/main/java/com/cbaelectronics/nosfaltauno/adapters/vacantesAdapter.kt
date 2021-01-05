/*
 * Created by Cba Electronics on 10/09/20 14:53
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 10/09/20 14:53
 */

package com.cbaelectronics.nosfaltauno.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.dataModel.dataVacantes
import kotlinx.android.synthetic.main.content_item_vacantes.view.*

class vacantesAdapter(private val context: Context): RecyclerView.Adapter<vacantesAdapter.ViewHolder>() {

    private var dataList = mutableListOf<dataVacantes>()

    fun setDataList(data: MutableList<dataVacantes>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vacantesAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_vacantes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: vacantesAdapter.ViewHolder, position: Int) {
        val vVacantes = dataList[position]
        holder.bindView(vVacantes)
    }

    override fun getItemCount(): Int {
        return if(dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(vacantes: dataVacantes){

            var vVacantes = ""
            if(vacantes.vacantes == 0){
                vVacantes = "PARTIDO COMPLETO"
            }else{
                vVacantes = "FALTAN\n" + vacantes.vacantes.toString()
            }

            itemView.nombreJugador1.text = vacantes.nombreJugador1
            Glide.with(context).load(vacantes.fotoJugador1).into(itemView.fotoJugador1)
            itemView.nombreJugador2.text = vacantes.nombreJugador2
            Glide.with(context).load(vacantes.fotoJugador2).into(itemView.fotoJugador2)
            itemView.nombreJugador3.text = vacantes.nombreJugador3
            Glide.with(context).load(vacantes.fotoJugador3).into(itemView.fotoJugador3)
            itemView.nombreJugador4.text = vacantes.nombreJugador4
            Glide.with(context).load(vacantes.fotoJugador4).into(itemView.fotoJugador4)
            itemView.detalleVacantes.text = vVacantes
        }
    }
}