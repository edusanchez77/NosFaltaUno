/*
 * Created by Cba Electronics on 04/09/20 09:35
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 04/09/20 09:35
 */

package com.cbaelectronics.nosfaltauno.dataModel

data class dataNotificacion (
    val notificacionId: String = "DEFAULT",
    val notifPartidoId: String = "DEFAULT",
    val notifStatus: String = "DEFAULT",
    val notifOrganizadorId: String = "DEFAULT",
    val notifUserId: String = "DEFAULT",
    val notifUserNombre: String = "DEFAULT",
    val notifUserFotoPerfil: String = "DEFAULT",
    val notifUserPosicion: String = "DEFAULT",
    val notifVacantes: Int
)