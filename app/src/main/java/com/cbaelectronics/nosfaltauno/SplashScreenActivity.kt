/*
 * Created by Cba Electronics on 25/9/20 12:36
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.postDelayed
import androidx.navigation.ActivityNavigator
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"
    val SPLASH_SCREEN = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var nextActivity = Intent(this, AuthActivity::class.java)

        val topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        logoApp.animation = topAnimation
        sloganApp.animation = bottomAnimation

        if(prefs.getString(key, "N") == "Y" ){
            nextActivity = Intent(this, HomeActivity::class.java)
        }

        Handler().postDelayed(Runnable {
            kotlin.run {
                val logoView = findViewById<View>(R.id.logoApp)


                var pair = Pair<View, String>(logoView, "imageTransition")

                val options = ActivityOptions
                    .makeSceneTransitionAnimation(this, pair)

                //val extras = ActivityNavigator.Extras(options)

                startActivity(nextActivity, options.toBundle())
                finish()
                //overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
            }
        }, SPLASH_SCREEN.toLong())

    }
}