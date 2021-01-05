package com.cbaelectronics.nosfaltauno.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.dataModel.dataJugadores
import kotlinx.android.synthetic.main.content_item_jugadores.view.*

class jugadoresAdapter(
    private val context: Context,
    private val itemClickListener: onClickJugadoresClickListener
):RecyclerView.Adapter<jugadoresAdapter.ViewHolder>() {

    interface onClickJugadoresClickListener{
        fun onItemClick(
            usrFotoperfil: String,
            usrNombre: String,
            usrCategory: String,
            usrPosition: String,
            itemView: View
        )
    }

    private var dataList = mutableListOf<dataJugadores>()

    fun setDataList(data: MutableList<dataJugadores>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): jugadoresAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_jugadores2, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    override fun onBindViewHolder(holder: jugadoresAdapter.ViewHolder, position: Int) {
        val jugadores = dataList[position]
        holder.bindView(jugadores)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(jugadores: dataJugadores){
            itemView.nombreJugador.text = jugadores.usr_nombre
            //itemView.categoriaJugador.text = jugadores.usr_category
            //itemView.posicionJugador.text = jugadores.usr_position
            Glide.with(context).load(jugadores.usr_fotoPerfil).into(itemView.fotoPerfilJugador)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(
                    jugadores.usr_fotoPerfil,
                    jugadores.usr_nombre,
                    jugadores.usr_category,
                    jugadores.usr_position,
                    itemView)
            }
        }
    }
}