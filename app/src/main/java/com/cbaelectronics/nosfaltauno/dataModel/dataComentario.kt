/*
 * Created by Cba Electronics on 27/08/20 17:41
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 27/08/20 17:41
 */

package com.cbaelectronics.nosfaltauno.dataModel

import java.util.*

data class dataComentario (
    val post_UsrNombre: String = "DEFAULT",
    val post_fecha: Date?,
    val post_fotoPerfil: String = "DEFAULT",
    val post_comentario: String = "DEFAULT"
)