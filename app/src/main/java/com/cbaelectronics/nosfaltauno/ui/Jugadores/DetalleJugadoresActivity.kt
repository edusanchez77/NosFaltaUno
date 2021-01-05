/*
 * Created by Cba Electronics on 29/9/20 12:21
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno.ui.Jugadores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import kotlinx.android.synthetic.main.activity_detalle_jugadores.*

class DetalleJugadoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_jugadores)

        val fotoPerfil = intent.extras?.getString("fotoPerfil")

        Glide.with(this).load(fotoPerfil).into(fotoPerfilDetalleJugadores)
        nombreDetalleJugador.text = intent.extras?.getString("usrNombre")
        categoriaDetalleJugador.text = intent.extras?.getString("usrCategoria")
        posicionDetalleJugador.text = intent.extras?.getString("usrPosition")

    }
}