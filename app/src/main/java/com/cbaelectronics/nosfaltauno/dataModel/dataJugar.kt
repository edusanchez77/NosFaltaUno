/*
 * Created by Cba Electronics on 20/08/20 16:14
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 20/08/20 16:14
 */

package com.cbaelectronics.nosfaltauno.dataModel

import java.util.*

data class dataJugar (
    val par_usrId: String = "DEFAULT_USRID",
    val par_partidoId: String = "DEFAULT_PARTIDOID",
    val par_nombrePartido: String = "DEFAULT_NOMBREPARTIDO",
    val par_complejo: String = "DEFAULT_COMPLEJO",
    val par_fechaPartido: Date?,
    val par_vacantes: String = "DEFAULT_VACANTES",
    val par_categoria: String = "DEFAULT_CATEGORIA",
    val par_genero: String = "DEFAULT_GENERO",
    val par_techo: String = "DEFAULT_TECHO",
    val par_piso: String = "DEFAULT_PISO",
    val par_pared: String = "DEFAULT_PARED",
    val par_fotoPerfil: String = "DEFAULT_FOTO",
    val par_nombreUser: String = "DEFAULT_NOMBRE",
    val par_tokenDispositivo: String = "DEFAULT_TOKEN",
    val par_solicitudes: Int = 0
)