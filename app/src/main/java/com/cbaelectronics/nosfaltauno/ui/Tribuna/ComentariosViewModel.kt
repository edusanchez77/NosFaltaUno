/*
 * Created by Cba Electronics on 28/08/20 14:59
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 28/08/20 14:59
 */

package com.cbaelectronics.nosfaltauno.ui.Tribuna

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.nosfaltauno.dataModel.dataComentario
import com.cbaelectronics.nosfaltauno.dataModel.dataJugadores
import com.cbaelectronics.nosfaltauno.dataModel.firebase.*


class ComentariosViewModel : ViewModel() {

    private val getFirestore = getFirestore()

    fun fetchComentariosData(pPostId: String): LiveData<MutableList<dataComentario>> {
        val mutableData = MutableLiveData<MutableList<dataComentario>>()

        getFirestore.getComentario(pPostId).observeForever {
            mutableData.value = it
        }

        return mutableData
    }

}