package com.cbaelectronics.nosfaltauno.ui.Jugadores

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.cbaelectronics.nosfaltauno.R
import com.cbaelectronics.nosfaltauno.adapters.jugadoresAdapter
import kotlinx.android.synthetic.main.content_item_jugadores.view.*
import kotlinx.android.synthetic.main.content_item_jugadores.view.fotoPerfilJugador
import kotlinx.android.synthetic.main.content_item_jugadores.view.nombreJugador
import kotlinx.android.synthetic.main.content_item_jugadores2.view.*
import kotlinx.android.synthetic.main.fragment_jugadores.view.*

class JugadoresFragment : Fragment(), jugadoresAdapter.onClickJugadoresClickListener {

    private lateinit var adapter: jugadoresAdapter
    private val viewModelJugadores by lazy { ViewModelProviders.of(this).get(JugadoresViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_jugadores, container, false)
        val context = root.context

        adapter = jugadoresAdapter(context, this)

        //root.recyclerViewJugadores.layoutManager = LinearLayoutManager(context)
        root.recyclerViewJugadores.layoutManager = GridLayoutManager(context, 2)
        root.recyclerViewJugadores.adapter = adapter
        //root.recyclerViewJugadores.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        observeData()

        return root
    }

    private fun observeData() {
        viewModelJugadores.fetchJugadoresData().observe(viewLifecycleOwner, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onItemClick(
        usrFotoperfil: String,
        usrNombre: String,
        usrCategory: String,
        usrPosition: String,
        itemView: View
    ) {
        val detalleJugadoresActivity = Intent(context, DetalleJugadoresActivity::class.java)
            .putExtra("fotoPerfil", usrFotoperfil)
            .putExtra("usrNombre", usrNombre)
            .putExtra("usrCategoria", usrCategory)
            .putExtra("usrPosition", usrPosition)

        var pair = Pair<View, String>(itemView.fotoPerfilJugador, "fotoDetalleJugador")
        var pair2 = Pair<View, String>(itemView.nombreJugador, "nombreDetalle")


        val options = ActivityOptions
            .makeSceneTransitionAnimation(context as Activity?, pair, pair2)

        startActivity(detalleJugadoresActivity, options.toBundle())

    }
}