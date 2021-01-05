/*
 * Created by Cba Electronics on 08/09/20 12:36
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 08/09/20 12:36
 */

package com.cbaelectronics.nosfaltauno.vo

import com.cbaelectronics.nosfaltauno.Interface.WebService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("http://www.nosfaltauno.com.ar/Android/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}