/*
 * Created by Cba Electronics on 08/09/20 14:50
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 08/09/20 14:50
 */

package com.cbaelectronics.nosfaltauno.vo

import java.lang.Exception

sealed class Resource<out T> {

    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val exception: Exception) : Resource<T>()

}