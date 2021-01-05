/*
 * Created by Cba Electronics on 26/08/20 16:09
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 26/08/20 16:09
 */

package com.cbaelectronics.nosfaltauno.ui.Tribuna

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Pair
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.adapters.tribunaAdapter
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import kotlinx.android.synthetic.main.activity_comentarios.view.*
import kotlinx.android.synthetic.main.content_item_jugar2.view.*
import kotlinx.android.synthetic.main.content_item_jugar2.view.jugarFotoPerfil
import kotlinx.android.synthetic.main.content_item_tribuna.view.*
import kotlinx.android.synthetic.main.fragment_tribuna.view.*
import java.text.SimpleDateFormat
import java.util.*

class TribunaFragment : Fragment(), tribunaAdapter.onPostClickListener {

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    private lateinit var adapter: tribunaAdapter
    private val viewModelTribuna by lazy { ViewModelProviders.of(this).get(TribunaViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tribuna, container, false)
        val context = root.context

        val setFirebase =
            setFirestore(root.context)

        val prefs = PreferenceManager.getDefaultSharedPreferences(root.context)

        adapter = tribunaAdapter(context, this)

        root.recyclerViewTribuna.layoutManager = LinearLayoutManager(context)
        root.recyclerViewTribuna.adapter = adapter

        observeData()

        root.btnEnviarMensaje.setOnClickListener {
            if (root.mensajeTribuna.text.isNullOrEmpty()){
                Toast.makeText(context, "No escribiste ningún posteo", Toast.LENGTH_SHORT).show()
            }else{
                //Toast.makeText(root.context, root.mensajeTribuna.text, Toast.LENGTH_SHORT).show()
                setFirebase.setPostTribuna(prefs.getString(usrId, "DEFAULT").toString(),
                    prefs.getString(usrNombre, "DEFAULT").toString(),
                    prefs.getString(usrFotoPerfil, "DEFAULT").toString(),
                    root.mensajeTribuna.text.toString())

                root.mensajeTribuna.text = null
                root.mensajeTribuna.clearFocus()
                val key = root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                key.hideSoftInputFromWindow(root.windowToken, 0)
            }

        }

        return root
    }

    private fun observeData() {
        viewModelTribuna.fetchPostData().observe(viewLifecycleOwner, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onClickPost(
        postId: String,
        postUsrId: String,
        postNombre: String,
        postFotoperfil: String,
        postMessage: String,
        postCantcomentarios: Int?,
        postFecha: Date?,
        itemView: View
    ) {
        val sfd = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var pair = Pair<View, String>(itemView.imagenPerfilPost, "fotoComentario")
        var pair2 = Pair<View, String>(itemView.mensajePost, "mensajeComentario")

        val options = ActivityOptions
            .makeSceneTransitionAnimation(context as Activity?, pair)

        startActivity(Intent(context, ComentariosActivity::class.java)
            .putExtra("postId", postId)
            .putExtra("postUsrId", postUsrId)
            .putExtra("postNombre", postNombre)
            .putExtra("postFotoPerfil", postFotoperfil)
            .putExtra("postMessage", postMessage)
            .putExtra("post_cantComentarios", postCantcomentarios)
            .putExtra("post_fecha", sfd.format(postFecha)), options.toBundle()
        )
    }

}