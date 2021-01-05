package com.cbaelectronics.nosfaltauno.ui.Partidos

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.nosfaltauno.DetalleMisPartidosActivity
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.adapters.jugarAdapter
import kotlinx.android.synthetic.main.fragment_partidos.view.*

class PartidosFragment : Fragment(), jugarAdapter.onPartidoClickListener {

    private val key             = "MY_KEY"
    private val usrId           = "MY_ID"
    private val usrNombre       = "MY_NOMBRE"
    private val usrEmail        = "MY_EMAIL"
    private val usrFotoPerfil   = "MY_FOTOPERFIL"

    private lateinit var adapter: jugarAdapter
    private val viewModelMisPartidos by lazy { ViewModelProviders.of(this).get(PartidosViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_partidos, container, false)

        val context = root.context

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        adapter = jugarAdapter(context, "Y" , this)

        root.recyclerViewMisPartidos.layoutManager = LinearLayoutManager(context)
        root.recyclerViewMisPartidos.adapter = adapter

        observeData(prefs.getString(usrId, "DEFAULT").toString(), root)


        return root
    }

    private fun observeData(pUsrId: String, root: View) {
        viewModelMisPartidos.fetchMisPartidosData(pUsrId).observe(viewLifecycleOwner, Observer {
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
        val detalleMisPartidosActivity = Intent(context, DetalleMisPartidosActivity::class.java)
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

        startActivity(detalleMisPartidosActivity)

    }
}