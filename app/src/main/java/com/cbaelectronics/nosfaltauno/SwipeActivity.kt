/*
 * Created by Cba Electronics on 16/9/20 11:30
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_swipe.*

class SwipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)

        refreshApp()
    }

    private fun refreshApp() {
        swipeRefresh.setOnRefreshListener {
            Toast.makeText(this, "Refresco correcto", Toast.LENGTH_SHORT).show()
            swipeRefresh.isRefreshing = false
        }


    }


}