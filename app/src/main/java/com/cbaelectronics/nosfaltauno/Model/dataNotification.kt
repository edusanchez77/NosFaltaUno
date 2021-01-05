/*
 * Created by Cba Electronics on 21/9/20 16:15
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class dataNotification(
    val success: Int = 0
) : Parcelable