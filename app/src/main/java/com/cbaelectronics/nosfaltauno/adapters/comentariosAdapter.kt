/*
 * Created by Cba Electronics on 27/08/20 17:44
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 27/08/20 17:44
 */

package com.cbaelectronics.nosfaltauno.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.dataModel.dataComentario
import kotlinx.android.synthetic.main.content_item_comentarios.view.*
import java.text.SimpleDateFormat

class comentariosAdapter(private val context: Context): RecyclerView.Adapter<comentariosAdapter.ViewHolder>(){

    private var dataList = mutableListOf<dataComentario>()

    fun setDataList(data: MutableList<dataComentario>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): comentariosAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_comentarios, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: comentariosAdapter.ViewHolder, position: Int) {
        val vComentarios = dataList[position]
        holder.bindView(vComentarios)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(comentarios: dataComentario){
            val sfd = SimpleDateFormat("dd/MM/yyyy HH:mm")

            itemView.comentarioNombre.text = comentarios.post_UsrNombre
            itemView.comentarioMessage.text = comentarios.post_comentario
            Glide.with(context).load(comentarios.post_fotoPerfil).into(itemView.comentarioFotoPerfil)
            itemView.comentarioFecha.text = sfd.format(comentarios.post_fecha)
        }
    }
}