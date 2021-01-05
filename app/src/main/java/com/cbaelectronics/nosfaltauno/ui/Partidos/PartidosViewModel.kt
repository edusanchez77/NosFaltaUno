package com.cbaelectronics.nosfaltauno.ui.Partidos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.nosfaltauno.dataModel.dataJugar
import com.cbaelectronics.nosfaltauno.dataModel.firebase.*

class PartidosViewModel : ViewModel() {

    private val getFirestore = getFirestore()

    fun fetchMisPartidosData(pUsrId: String): LiveData<MutableList<dataJugar>>{
        val mutableData = MutableLiveData<MutableList<dataJugar>>()

        getFirestore.getMisPartidosData(pUsrId).observeForever {
            mutableData.value = it
        }

        return mutableData
    }
}