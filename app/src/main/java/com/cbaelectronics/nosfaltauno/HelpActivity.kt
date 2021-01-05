/**
 * Created by Cba Electronics on 12/08/20 18:07
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 12/08/20 18:07
 */

package com.cbaelectronics.nosfaltauno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.cbaelectronics.nosfaltauno.adapters.slideHelpAdapter
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        viewPagerSlide.adapter = slideHelpAdapter(supportFragmentManager)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}