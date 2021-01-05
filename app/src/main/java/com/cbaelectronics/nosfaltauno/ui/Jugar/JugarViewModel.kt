package com.cbaelectronics.nosfaltauno.ui.Jugar

import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.nosfaltauno.dataModel.dataJugar
import com.cbaelectronics.nosfaltauno.dataModel.firebase.getFirestore

class JugarViewModel : ViewModel() {

    private val getFirestore = getFirestore()

    fun fetchPartidosData(pUsrId: String): LiveData<MutableList<dataJugar>>{
        val mutableData = MutableLiveData<MutableList<dataJugar>>()

        getFirestore.getPartidosData(pUsrId).observeForever {
            mutableData.value = it
        }

        return mutableData
    }

}