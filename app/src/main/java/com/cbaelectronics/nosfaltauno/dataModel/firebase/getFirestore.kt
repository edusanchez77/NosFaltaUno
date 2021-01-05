/**
 * Created by Cba Electronics on 12/08/20 14:57
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 12/08/20 14:57
 */

package com.cbaelectronics.nosfaltauno.dataModel.firebase

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cbaelectronics.nosfaltauno.dataModel.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Timestamp
import java.util.*
import kotlin.system.exitProcess

class getFirestore {

    /**
     * Funcion getJugadores
     */
    fun getJugadoresData(): LiveData<MutableList<dataJugadores>>{
        val mutableData = MutableLiveData<MutableList<dataJugadores>>()

        FirebaseFirestore.getInstance().collection("user")
            .orderBy("usr_nombre")
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<dataJugadores>()
                for (document in value!!){
                    val usr_nombre = document.getString("usr_nombre")
                    val usr_category = document.getString("usr_category")
                    val usr_position = document.getString("usr_position")
                    val usr_fotoPerfil = document.getString("usr_fotoPerfil")

                    val vJugadores = dataJugadores(
                        usr_nombre!!,
                        usr_category!!,
                        usr_position!!,
                        usr_fotoPerfil!!
                    )

                    listData.add(vJugadores)
                }

                mutableData.value = listData
            }

        return mutableData
    }

    /**
     * Funcion getPartidos
     */
    fun getPartidosData(pUsrId: String): LiveData<MutableList<dataJugar>>{
        val mutableData = MutableLiveData<MutableList<dataJugar>>()

        FirebaseFirestore.getInstance().collection("partidos")
            .orderBy("par_fechaPartido", Query.Direction.ASCENDING)
            .whereGreaterThanOrEqualTo("par_fechaPartido", Timestamp(Date().time))
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<dataJugar>()
                var datosUser = mapOf<String, String>()

                var jugador1 = "DEFAULT"
                var jugador2 = "DEFAULT"
                var jugador3 = "DEFAULT"
                var jugador4 = "DEFAULT"

                for (document in value!!){
                    datosUser = document.data.get("datosUser") as Map<String, String>
                    val par_usrId = datosUser.get("usr_id").toString()

                    jugador1 = document.getString("jugador1").toString()
                    jugador2 = document.getString("jugador2").toString()
                    jugador3 = document.getString("jugador3").toString()
                    jugador4 = document.getString("jugador4").toString()

                    if(par_usrId != pUsrId &&
                        jugador1 != pUsrId &&
                        jugador2 != pUsrId &&
                        jugador3 != pUsrId &&
                        jugador4 != pUsrId){

                        val par_partidoId = document.id
                        //val par_usrId = datosUser.get("usr_id").toString()
                        val par_fotoPerfil = datosUser.get("usr_fotoPerfil").toString()
                        val par_userName = datosUser.get("usr_nombre").toString()
                        val par_tokenDispositivo = datosUser.get("usr_tokenDispositivo").toString()
                        val par_nombrePartido= document.getString("par_nombrePartido")
                        val par_complejo = document.getString("par_complejo")
                        val par_fechaPartido = document.getDate("par_fechaPartido")
                        val par_vacantes = document.getLong("par_vacantes")?.toString()
                        val par_categoria = document.getString("par_categoria")
                        val par_genero = document.getString("par_genero")
                        val par_techo = document.getString("par_techo")
                        val par_piso = document.getString("par_piso")
                        val par_pared = document.getString("par_pared")
                        val par_solicitudes = document.getLong("par_solicitudes")

                        val vPartidos = dataJugar(
                            par_usrId,
                            par_partidoId,
                            par_nombrePartido!!,
                            par_complejo!!,
                            par_fechaPartido!!,
                            par_vacantes!!,
                            par_categoria!!,
                            par_genero!!,
                            par_techo!!,
                            par_piso!!,
                            par_pared!!,
                            par_fotoPerfil,
                            par_userName,
                            par_tokenDispositivo,
                            par_solicitudes!!.toInt()
                        )

                        listData.add(vPartidos)

                    }

                }

                mutableData.value = listData
            }

        return mutableData
    }


    /**
     * Funcion para obtener las vacantes de los partidos
     */
    fun getVacantes(pPartidoId: String): LiveData<MutableList<dataVacantes>>{
        val mutableData = MutableLiveData<MutableList<dataVacantes>>()

        FirebaseFirestore.getInstance().collection("detallePartidos").document(pPartidoId)
            .addSnapshotListener { value, error ->
                    val listData = mutableListOf<dataVacantes>()
                    var jugador1 = mapOf<String, String>()
                    var jugador2 = mapOf<String, String>()
                    var jugador3 = mapOf<String, String>()
                    var jugador4 = mapOf<String, String>()
                    jugador1 = value?.data?.get("jugador1") as Map<String, String>
                    jugador2 = value?.data?.get("jugador2") as Map<String, String>
                    jugador3 = value?.data?.get("jugador3") as Map<String, String>
                    jugador4 = value?.data?.get("jugador4") as Map<String, String>
                    val vacantes = value?.data?.get("dte_vacantes")


                    val detalle = dataVacantes(
                        jugador1.get("usr_nombre").toString(), jugador1.get("usr_fotoPerfil").toString(),
                        jugador2.get("usr_nombre").toString(), jugador2.get("usr_fotoPerfil").toString(),
                        jugador3.get("usr_nombre").toString(), jugador3.get("usr_fotoPerfil").toString(),
                        jugador4.get("usr_nombre").toString(), jugador4.get("usr_fotoPerfil").toString(),
                        vacantes.toString().toInt()
                    )

                    listData.add(detalle)

                    mutableData.value = listData

            }

        return mutableData

        }





    /**
     * Funcion para obtener los partidos organizados por mi
     */
    fun getMisPartidosData(pUsrId: String): LiveData<MutableList<dataJugar>>{
        val mutableData = MutableLiveData<MutableList<dataJugar>>()

        FirebaseFirestore.getInstance().collection("partidos")
            .orderBy("par_fechaPartido", Query.Direction.ASCENDING)
            .whereGreaterThanOrEqualTo("par_fechaPartido", Timestamp(Date().time))
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<dataJugar>()
                var datosUser = mapOf<String, String>()

                var jugador1 = "DEFAULT"
                var jugador2 = "DEFAULT"
                var jugador3 = "DEFAULT"
                var jugador4 = "DEFAULT"


                for (document in value!!){

                    datosUser = document.data.get("datosUser") as Map<String, String>

                    jugador1 = document.getString("par_jugador1").toString()
                    jugador2 = document.getString("par_jugador2").toString()
                    jugador3 = document.getString("par_jugador3").toString()
                    jugador4 = document.getString("par_jugador4").toString()

                    val par_usrId = datosUser.get("usr_id").toString()

                    if(par_usrId ==  pUsrId ||
                        jugador1 == pUsrId ||
                        jugador2 == pUsrId ||
                        jugador3 == pUsrId ||
                        jugador4 == pUsrId){

                        val par_partidoId = document.id
                        //val par_usrId = datosUser.get("usr_id").toString()
                        val par_fotoPerfil = datosUser.get("usr_fotoPerfil").toString()
                        val par_userName = datosUser.get("usr_nombre").toString()
                        val par_tokenDispositivo = datosUser.get("usr_tokenDispositivo").toString()
                        val par_nombrePartido= document.getString("par_nombrePartido")
                        val par_complejo = document.getString("par_complejo")
                        val par_fechaPartido = document.getDate("par_fechaPartido")
                        val par_vacantes = document.getLong("par_vacantes")?.toString()
                        val par_categoria = document.getString("par_categoria")
                        val par_genero = document.getString("par_genero")
                        val par_techo = document.getString("par_techo")
                        val par_piso = document.getString("par_piso")
                        val par_pared = document.getString("par_pared")
                        val par_solicitudes = document.getLong("par_solicitudes")

                        val vPartidos = dataJugar(
                            par_usrId,
                            par_partidoId,
                            par_nombrePartido!!,
                            par_complejo!!,
                            par_fechaPartido!!,
                            par_vacantes!!,
                            par_categoria!!,
                            par_genero!!,
                            par_techo!!,
                            par_piso!!,
                            par_pared!!,
                            par_fotoPerfil,
                            par_userName,
                            par_tokenDispositivo,
                            par_solicitudes!!.toInt()
                        )

                        listData.add(vPartidos)

                    }


                }

                mutableData.value = listData
            }

        return mutableData
    }



    /**
     * Funcion para obtener las notificaciones
     */
    fun getNotification(pPartidoId: String, pUsrId: String): LiveData<MutableList<dataNotificacion>>{
        val mutableData = MutableLiveData<MutableList<dataNotificacion>>()

        FirebaseFirestore.getInstance().collection("notificaciones")
            .whereEqualTo("not_organizadorId", pUsrId)
            .whereEqualTo("not_partidoId", pPartidoId)
            .whereEqualTo("not_status", "P")
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<dataNotificacion>()
                var datosUser = mapOf<String, String>()

                for (document in value!!){
                    datosUser = document.data.get("datosUser") as Map<String, String>

                    val vNotificacionId = document.id
                    val vPartidoId = document.getString("not_partidoId")
                    val vNotifStatus = document.getString("not_status")
                    val vOrganizadorId = document.getString("not_organizadorId")
                    val vUsrId = datosUser.get("usr_id")
                    val vFotoPerfil = datosUser.get("usr_fotoPerfil")
                    val vNombreUser = datosUser.get("usr_nombre")
                    val vUserPosition = datosUser.get("usr_position")
                    val vNotifVacantes = document.getLong("not_vacantes").toString().toInt()

                    val vNotificaciones = dataNotificacion(
                        vNotificacionId,
                        vPartidoId!!,
                        vNotifStatus!!,
                        vOrganizadorId!!,
                        vUsrId!!,
                        vNombreUser!!,
                        vFotoPerfil!!,
                        vUserPosition!!,
                        vNotifVacantes!!
                    )

                    listData.add(vNotificaciones)
                }

                mutableData.value = listData
            }

        return mutableData
    }


    /**
     * Function para validar si ya estoy postulado a un partido
     */
    fun getPostulation(pUsrId: String, pPartidoId: String): Int{

        var vPostulate = 0

        FirebaseFirestore.getInstance().collection("notificaciones")
            //.whereEqualTo("not_partidoId", pPartidoId)
            //.whereEqualTo("datosUser.usr_id", pUsrId)
            //.whereEqualTo("not_status", "P")
            .get()
            .addOnSuccessListener {
                vPostulate = it.size()
            }
            .addOnFailureListener {
                vPostulate = 5
            }

        return vPostulate
    }



    /**
     * getPost
     */
    fun getPost(): LiveData<MutableList<dataTribuna>>{
        val mutableData = MutableLiveData<MutableList<dataTribuna>>()

        FirebaseFirestore.getInstance().collection("tribuna")
            .orderBy("post_addDate", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<dataTribuna>()

                for (document in value!!){
                    val vPostId = document.id
                    val vUsrId = document.getString("post_usrId")
                    val vUsrNombre = document.getString("post_usrNombre")
                    val vUsrFoto = document.getString("post_usrFotoPerfil")
                    val vMessage = document.getString("post_message")
                    val vAddDate = document.getDate("post_addDate")
                    val vCantComentarios = document.getLong("post_cantComentarios")?.toInt()

                    val vPost = dataTribuna(
                        vPostId!!,
                        vUsrId!!,
                        vUsrNombre!!,
                        vUsrFoto!!,
                        vAddDate,
                        vMessage!!,
                        vCantComentarios!!
                    )

                    listData.add(vPost)
                }

                mutableData.value = listData
            }

        return mutableData
    }


    /**
     * getComentario
     */
    fun getComentario(pPostId: String): LiveData<MutableList<dataComentario>>{
        val mutableData = MutableLiveData<MutableList<dataComentario>>()

        FirebaseFirestore.getInstance().collection("comentarios")
            //.whereEqualTo("com_idPost", pPostId)
            .orderBy("com_addDate", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<dataComentario>()
                var datosUser = mapOf<String, String>()

                for (document in value!!){
                    if (document.getString("com_idPost").toString() == pPostId){
                        datosUser = document.data.get("com_datosUser") as Map<String, String>
                        val com_fotoPerfil = datosUser.get("usr_fotoPerfil")
                        val com_userName = datosUser.get("usr_nombre")

                        val vMessage = document.getString("com_message")
                        val vAddDate = document.getDate("com_addDate")

                        val vComentario = dataComentario(
                            com_userName!!,
                            vAddDate,
                            com_fotoPerfil!!,
                            vMessage!!
                        )

                        listData.add(vComentario)
                    }

                }

                mutableData.value = listData
            }

        return mutableData
    }


}