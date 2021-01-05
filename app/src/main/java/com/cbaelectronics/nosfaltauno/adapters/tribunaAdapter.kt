/*
 * Created by Cba Electronics on 26/08/20 17:34
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 26/08/20 17:34
 */

package com.cbaelectronics.nosfaltauno.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.dataModel.dataTribuna
import kotlinx.android.synthetic.main.content_item_tribuna.view.*
import java.text.SimpleDateFormat
import java.util.*

class tribunaAdapter(
    private val context: Context,
    private val itemClickListener: onPostClickListener
): RecyclerView.Adapter<tribunaAdapter.ViewHolder>() {

    interface onPostClickListener{
        fun onClickPost(
            postId: String,
            postUsrId: String,
            postNombre: String,
            postFotoperfil: String,
            postMessage: String,
            postCantcomentarios: Int?,
            postFecha: Date?,
            itemView: View
        )
    }

    private var dataList = mutableListOf<dataTribuna>()

    fun setDataList(data: MutableList<dataTribuna>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): tribunaAdapter.ViewHolder {
        val View = LayoutInflater.from(context).inflate(
            R.layout.content_item_tribuna,
            parent,
            false
        )
        return ViewHolder(View)
    }

    override fun onBindViewHolder(holder: tribunaAdapter.ViewHolder, position: Int) {
        val vPost = dataList[position]
        holder.bindHolder(vPost)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindHolder(post: dataTribuna){

            itemView.setOnClickListener {
                itemClickListener.onClickPost(
                    post.post_id,
                    post.post_usrId,
                    post.post_nombre,
                    post.post_fotoPerfil,
                    post.post_message,
                    post.post_cantComentarios,
                    post.post_fecha,
                    itemView
                )
            }

            val sfd = SimpleDateFormat("dd/MM/yyyy HH:mm")

            itemView.nombrePost.text = post.post_nombre
            itemView.fechaPost.text = sfd.format(post.post_fecha)
            itemView.mensajePost.text = post.post_message
            Glide.with(context).load(post.post_fotoPerfil).into(itemView.imagenPerfilPost)
            itemView.comentariosPost.text = post.post_cantComentarios.toString() + " comentarios"
        }
    }
}