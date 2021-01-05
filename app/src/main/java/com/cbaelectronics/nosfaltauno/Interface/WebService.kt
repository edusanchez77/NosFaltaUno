/*
 * Created by Cba Electronics on 08/09/20 12:31
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 08/09/20 12:31
 */

package com.cbaelectronics.nosfaltauno.Interface

import com.cbaelectronics.nosfaltauno.Model.dataNotification
import com.cbaelectronics.nosfaltauno.Model.dataUsers
import retrofit2.Call
import retrofit2.http.GET

interface WebService {

    @GET("wsGetUser.php")
    suspend fun getUsers(): Call<List<dataUsers>>

    @GET("sendNotification.php")
    fun sendNotification(): Call<dataNotification>
}