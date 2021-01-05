/*
 * Created by Cba Electronics on 26/08/20 16:09
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 26/08/20 16:09
 */

package com.cbaelectronics.nosfaltauno.ui.Tribuna

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.nosfaltauno.dataModel.dataTribuna
import com.cbaelectronics.nosfaltauno.dataModel.firebase.getFirestore

class TribunaViewModel : ViewModel() {
    private val getFirestore = getFirestore()

    fun fetchPostData(): LiveData<MutableList<dataTribuna>> {
        val mutableData = MutableLiveData<MutableList<dataTribuna>>()

        getFirestore.getPost().observeForever {
            mutableData.value = it
        }

        return mutableData
    }
}