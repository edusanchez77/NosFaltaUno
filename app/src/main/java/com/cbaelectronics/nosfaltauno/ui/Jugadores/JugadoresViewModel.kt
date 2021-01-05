package com.cbaelectronics.nosfaltauno.ui.Jugadores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.nosfaltauno.dataModel.dataJugadores
import com.cbaelectronics.nosfaltauno.dataModel.firebase.getFirestore

class JugadoresViewModel : ViewModel() {

    private val getFirestore = getFirestore()

    fun fetchJugadoresData(): LiveData<MutableList<dataJugadores>>{
        val mutableData = MutableLiveData<MutableList<dataJugadores>>()

        getFirestore.getJugadoresData().observeForever {
            mutableData.value = it
        }

        return mutableData
    }

}