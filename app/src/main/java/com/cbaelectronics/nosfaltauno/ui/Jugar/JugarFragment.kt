package com.cbaelectronics.nosfaltauno.ui.Jugar

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.nosfaltauno.CrearPartidoActivity
import com.cbaelectronics.nosfaltauno.DetallePartidosActivity
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.adapters.jugarAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_item_jugadores2.view.*
import kotlinx.android.synthetic.main.content_item_jugar2.view.*
import kotlinx.android.synthetic.main.fragment_jugar.view.*

class JugarFragment : Fragment(), jugarAdapter.onPartidoClickListener {

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    private lateinit var adapter: jugarAdapter
    private val viewModelJugar by lazy { ViewModelProviders.of(this).get(JugarViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_jugar, container, false)
        val context = root.context

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        adapter = jugarAdapter(context, "N",this)

        root.recyclerViewJugar.layoutManager = LinearLayoutManager(context)
        root.recyclerViewJugar.adapter = adapter

        //root.recyclerViewJugar.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        observeData(prefs.getString(usrId, "DEFAULT").toString(), root)

        root.fab.setOnClickListener { view ->
            Snackbar.make(view, "Organizar partido", Snackbar.LENGTH_LONG)
                startActivity(Intent(root.context, CrearPartidoActivity::class.java))
        }

        return root
    }

    private fun observeData(pUsrId: String, root: View) {
        viewModelJugar.fetchPartidosData(pUsrId).observe(viewLifecycleOwner, Observer {
            if (it.size > 0){
                root.txtNoHayPartidos.visibility = View.GONE
            }
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onItemClick(
        parPartidoId: String,
        parNombrepartido: String,
        parCategoria: String,
        parFecha: String,
        parGenero: String,
        parVacantes: String,
        parComplejo: String,
        parFotoPerfil: String,
        parNombreUser: String,
        parUserId: String,
        parSolicitudes: Int,
        parTecho: String,
        parPiso: String,
        parPared: String,
        parTokendispositivo: String,
        itemView: View
    ) {
        val detallePartidos = Intent(context, DetallePartidosActivity::class.java)
            .putExtra("fotoPerfil", parFotoPerfil)
            .putExtra("nombreUsuario", parNombreUser)
            .putExtra("userId", parUserId)
            .putExtra("nombrePartido", parNombrepartido)
            .putExtra("categoria", parCategoria)
            .putExtra("genero", parGenero)
            .putExtra("vacantes", parVacantes)
            .putExtra("complejo", parComplejo)
            .putExtra("fecha", parFecha)
            .putExtra("partidoId", parPartidoId)
            .putExtra("solicitudes", parSolicitudes)
            .putExtra("techo", parTecho)
            .putExtra("piso", parPiso)
            .putExtra("pared", parPared)
            .putExtra("tokenDispositivo", parTokendispositivo)

        var pair = Pair<View, String>(itemView.jugarFotoPerfil, "fotoPerfilTransition")


        val options = ActivityOptions
            .makeSceneTransitionAnimation(context as Activity?, pair)

        startActivity(detallePartidos, options.toBundle())
    }
}