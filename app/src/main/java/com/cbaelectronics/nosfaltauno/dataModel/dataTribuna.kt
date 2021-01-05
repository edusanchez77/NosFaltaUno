/*
 * Created by Cba Electronics on 26/08/20 17:32
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 26/08/20 17:32
 */

package com.cbaelectronics.nosfaltauno.dataModel

import java.util.*

data class dataTribuna(
    val post_id: String = "DEFAULT",
    val post_usrId: String = "DEFAULT",
    val post_nombre: String = "DEFAULT",
    val post_fotoPerfil: String = "DEFAULT",
    val post_fecha: Date?,
    val post_message: String = "DEFAULT",
    val post_cantComentarios: Int?
)