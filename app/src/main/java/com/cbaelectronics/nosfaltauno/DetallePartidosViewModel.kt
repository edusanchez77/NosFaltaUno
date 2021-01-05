/*
 * Created by Cba Electronics on 02/09/20 16:19
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 02/09/20 16:19
 */

package com.cbaelectronics.nosfaltauno
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.nosfaltauno.dataModel.dataNotificacion
import com.cbaelectronics.nosfaltauno.dataModel.dataVacantes
import com.cbaelectronics.nosfaltauno.dataModel.firebase.*

class DetallePartidosViewModel: ViewModel() {

    private val getFirestore = getFirestore()

    fun fetchNotificacionesData(pPartidoId: String, pUserId: String): LiveData<MutableList<dataNotificacion>> {
        val mutableData = MutableLiveData<MutableList<dataNotificacion>>()

        getFirestore.getNotification(pPartidoId, pUserId).observeForever {
            mutableData.value = it
        }

        return mutableData
    }

    fun fetchVacantesData(pPartidoId: String): LiveData<MutableList<dataVacantes>>{
        val mutableData = MutableLiveData<MutableList<dataVacantes>>()

        getFirestore.getVacantes(pPartidoId).observeForever{
            mutableData.value = it
        }

        return mutableData
    }


}