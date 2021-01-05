/*
 * Created by Cba Electronics on 08/09/20 16:53
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 08/09/20 16:52
 */

package com.cbaelectronics.nosfaltauno.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class dataUsers (
    @SerializedName("usr_id")
    val usrId:String = "",
    @SerializedName("usr_nombre")
    val usrNombre:String = "",
    @SerializedName("usr_email")
    val usrEmail:String = "",
    @SerializedName("usr_fotoPerfil")
    val usrFotoPerfil:String = "",
    @SerializedName("usr_position")
    val usrPosition:String = "",
    @SerializedName("usr_categoria")
    val usrCategory:String = ""
): Parcelable


data class UsersList(
    @SerializedName("users")
    val usrList: List<dataUsers>)