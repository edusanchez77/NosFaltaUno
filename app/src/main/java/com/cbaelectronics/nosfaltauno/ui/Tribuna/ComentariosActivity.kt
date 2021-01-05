/*
 * Created by Cba Electronics on 02/09/20 16:17
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 31/08/20 16:22
 */

package com.cbaelectronics.nosfaltauno.ui.Tribuna

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.adapters.comentariosAdapter
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import kotlinx.android.synthetic.main.activity_comentarios.*
import java.text.SimpleDateFormat

class ComentariosActivity : AppCompatActivity() {
    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    private lateinit var adapter: comentariosAdapter
    private val viewModelComentarios by lazy { ViewModelProviders.of(this).get(ComentariosViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comentarios)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val setFirebase = setFirestore(this)


        val vPostId = intent.extras?.getString("postId")
        val vUsrId = intent.extras?.getString("postUsrId")
        val vNombre = intent.extras?.getString("postNombre")
        val vFotoPerfil = intent.extras?.getString("postFotoPerfil")
        val vMessage = intent.extras?.getString("postMessage")
        val vCantComentarios = intent.extras?.getInt("post_cantComentarios")
        val vPostFecha = intent.extras?.getString("post_fecha")

        Glide.with(this).load(vFotoPerfil).into(postFotoPerfil)
        postFecha.text = vPostFecha
        //postNombreUser.text = vNombre
        postMessage.text = vMessage
        if (vCantComentarios == 0){
            cantidadComentarios.visibility = View.VISIBLE
        }else{
            cantidadComentarios.visibility = View.GONE
        }

        adapter = comentariosAdapter(this)
        recyclerViewPost.layoutManager = LinearLayoutManager(this)
        recyclerViewPost.adapter = adapter

        observeData(vPostId)

        btnEnviarComentario.setOnClickListener {

            if (editTextComentario.text.isNullOrEmpty()){
                    Toast.makeText(this, "No escribiste ningún comentario", Toast.LENGTH_SHORT).show()
            }else{
                setFirebase.setComentarioPost(vPostId.toString(),
                    prefs.getString(usrId, "DEFAULT").toString(),
                    prefs.getString(usrNombre, "DEFAULT").toString(),
                    prefs.getString(usrFotoPerfil, "DEFAULT").toString(),
                    editTextComentario.text.toString(),
                    vCantComentarios!!
                )

                editTextComentario.text = null
                hideKeyboard()
            }

        }


    }

    private fun observeData(vPostId: String?) {
        viewModelComentarios.fetchComentariosData(vPostId.toString()).observe(this, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        // else {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        // }
    }
}