package com.cbaelectronics.nosfaltauno.dataModel.firebase

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.util.*

class setFirestore(pContext: Context) {

    private val vContext = pContext
    private val db = FirebaseFirestore.getInstance()

    /**
     * Crear usuario
     */
    fun setUser(
        pUserId: String,
        pUserName: String,
        pUserApellido: String,
        pUserEmail: String,
        pUserPhoto: String,
        pUserCategory: String,
        pUserPosition: String,
        pCredential: String,
        pTokenDispositivo: String
    ){

        var vApellido: String? = ""

        if (pCredential == "GOOGLE"){
            vApellido = ""
        }else{
            vApellido = pUserApellido
        }
        val user = hashMapOf(
            "usr_nombre" to "$pUserName $vApellido",
            "usr_email" to pUserEmail,
            "usr_fotoPerfil" to pUserPhoto,
            "usr_category" to pUserCategory,
            "usr_position" to pUserPosition,
            "usr_addDate" to Timestamp(Date().time),
            "usr_tokenDispositivo" to pTokenDispositivo
        )


        db.collection("user").document(pUserId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(vContext, "Bienvenido", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(vContext, "ERROR "+it.toString(), Toast.LENGTH_LONG).show()
            }

    }


    /**
     * Completar registro mediante Google
     */
    fun updatePerfil(pUserId: String, pUserCategory: String, pUserPosition: String){
        var user: Map<String, String> = mapOf()

        user = mutableMapOf(
            "usr_category" to pUserCategory,
            "usr_position" to pUserPosition
        )

        db.collection("user").document(pUserId)
            .update(user)
            .addOnSuccessListener {
                mostrarToast("Perfil Actualizado")
            }
            .addOnFailureListener {
                mostrarToast("Error $it")
            }
    }


    /**
     * Actualizar datos del usuario
     */
    fun updateUser(pUserId: String, pUserName: String?, pUserPhoto: String?, pUserCategory: String?, pUserPosition: String?){

        var vProgress = ProgressDialog(vContext)
        vProgress.setMessage("Actualizando la información")
        vProgress.show()

        var user: Map<String, String> = mapOf()
        user = mutableMapOf("usr_updatedDate" to Timestamp(Date().time).toString())

        var vFlagFoto = false
        var vFlagNombre = false

        if (!pUserName.isNullOrEmpty()){
            user.put("usr_nombre", pUserName)
            vFlagNombre = true
        }

        if(!pUserPhoto.isNullOrEmpty()){
            user.put("usr_fotoPerfil", pUserPhoto)
            vFlagFoto = true
        }
        if(!pUserCategory.isNullOrEmpty()){
            user.put("usr_category", pUserCategory)
        }
        if(!pUserPosition.isNullOrBlank()){
            user.put("usr_position", pUserPosition)
        }

        db.collection("user").document(pUserId)
            .update(user)
            .addOnSuccessListener {
                vProgress.dismiss()
                Toast.makeText(vContext, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                vProgress.dismiss()
                Toast.makeText(vContext, "ERROR "+it.toString(), Toast.LENGTH_SHORT).show()
            }

        if(vFlagFoto || vFlagNombre){

            val batch = db.batch()

            db.collection("partidos")
                .whereEqualTo("datosUser.usr_id", pUserId)
                .addSnapshotListener { value, error ->
                    for (document in value!!){
                        var docPartido = db.collection("partidos").document(document.id)
                        docPartido.update("datosUser.usr_fotoPerfil", pUserPhoto)
                    }

                }

            db.collection("detallePartidos")
                .addSnapshotListener { value, error ->
                    for (document in value!!){
                        if (document.getString("jugador1.usr_id") == pUserId){
                            db.collection("detallePartidos").document(document.id)
                                .update("jugador1.usr_fotoPerfil", pUserPhoto)
                        }else if (document.getString("jugador2.usr_id") == pUserId) {
                            db.collection("detallePartidos").document(document.id)
                                .update("jugador2.usr_fotoPerfil", pUserPhoto)
                        }else if (document.getString("jugador3.usr_id") == pUserId) {
                            db.collection("detallePartidos").document(document.id)
                                .update("jugador3.usr_fotoPerfil", pUserPhoto)
                        }else if (document.getString("jugador4.usr_id") == pUserId) {
                            db.collection("detallePartidos").document(document.id)
                                .update("jugador4.usr_fotoPerfil", pUserPhoto)
                        }
                    }
                }

            db.collection("notificaciones")
                .addSnapshotListener { value, error ->
                    for (document in value!!){
                        if (document.getString("datosUser.usr_id") == pUserId){
                            db.collection("notificaciones").document(document.id)
                                .update("datosUser.usr_fotoPerfil", pUserPhoto)
                        }
                    }
                }

        }
    }

    /**
     * Crear Partido
     */
    fun setPartido(
        pUserId: String,
        pNombrePartido: String?,
        pComplejo: String,
        pFechaPartido: Date?,
        pVacantes: Int,
        pCategory: String,
        pGenero: String,
        pTecho: String,
        pPiso: String,
        pPared: String,
        pFotoPerfil: String,
        pNombreUsuario: String,
        pTokenDispositivo: String
    ){

        showProgressBar("Organizando partido")

        var vNombrePartido = pNombrePartido

        if (pNombrePartido.isNullOrEmpty()){
            vNombrePartido = "Partido"
        }

        val vFotoPerfilOcupado = "https://firebasestorage.googleapis.com/v0/b/nos-falta-uno-d5f3c.appspot.com/o/fotoPerfil%2Fic_ocupado.png?alt=media&token=f4b64435-92f5-40df-9a11-ac556ec4e192"
        val vFotoPerfilLibre = "https://firebasestorage.googleapis.com/v0/b/nos-falta-uno-d5f3c.appspot.com/o/fotoPerfil%2Fic_libre.png?alt=media&token=44fe76ba-ea85-45dd-888b-f9dd45a3ac2f"
        val vVacanteOcupada = mutableMapOf(
                "usr_fotoPerfil" to vFotoPerfilOcupado,
                "usr_nombre" to "OCUPADO"
        )
        val vVacanteLibre = mutableMapOf(
            "usr_fotoPerfil" to vFotoPerfilLibre,
            "usr_nombre" to "LIBRE"
        )

        var datosUser: Map<String, String> = mapOf()

        datosUser = mutableMapOf("usr_id" to pUserId,
            "usr_fotoPerfil" to pFotoPerfil,
            "usr_nombre" to pNombreUsuario,
            "usr_tokenDispositivo" to pTokenDispositivo)

        var vJugador1: Map<String, String> = mapOf()
        var vJugador2: Map<String, String> = mapOf()
        var vJugador3: Map<String, String> = mapOf()
        var vJugador4: Map<String, String> = mapOf()


        when(pVacantes){
            1 -> {
                vJugador1 = datosUser
                vJugador2 = vVacanteOcupada
                vJugador3 = vVacanteOcupada
                vJugador4 = vVacanteLibre
            }
            2 -> {
                vJugador1 = datosUser
                vJugador2 = vVacanteOcupada
                vJugador3 = vVacanteLibre
                vJugador4 = vVacanteLibre
            }
            3 -> {
                vJugador1 = datosUser
                vJugador2 = vVacanteLibre
                vJugador3 = vVacanteLibre
                vJugador4 = vVacanteLibre
            }
            4 -> {
                vJugador1 = vVacanteLibre
                vJugador2 = vVacanteLibre
                vJugador3 = vVacanteLibre
                vJugador4 = vVacanteLibre
            }
        }




        val vPartido = hashMapOf(
            "par_nombrePartido" to vNombrePartido,
            "par_complejo" to pComplejo,
            "par_fechaPartido" to pFechaPartido,
            "par_vacantes" to pVacantes,
            "par_categoria" to pCategory,
            "par_genero" to pGenero,
            "par_techo" to pTecho,
            "par_piso" to pPiso,
            "par_pared" to pPared,
            "par_solicitudes" to 0,
            "datosUser" to datosUser
        )

        db.collection("partidos")
            .add(vPartido)
            .addOnSuccessListener { documentReference ->
                val vPartidoId = documentReference.id

                val vDetallePartido = hashMapOf(
                    "dte_vacantes" to pVacantes,
                    "jugador1" to vJugador1,
                    "jugador2" to vJugador2,
                    "jugador3" to vJugador3,
                    "jugador4" to vJugador4
                )

                db.collection("detallePartidos").document(vPartidoId)
                    .set(vDetallePartido)
                    .addOnSuccessListener {

                        hideProgressBar()
                        mostrarToast("Partido organizado correctamente")
                    }
                    .addOnFailureListener {
                        hideProgressBar()
                        mostrarToast("Error $it")
                    }
            }
            .addOnFailureListener {
                hideProgressBar()
                mostrarToast("Error $it")
            }

    }

    private fun hideProgressBar() {
        ProgressDialog(vContext).dismiss()
    }

    private fun showProgressBar(message: String) {
        val vProgress = ProgressDialog(vContext)
        vProgress.setMessage(message)
        vProgress.show()

    }

    private fun mostrarToast(message: String) {
        Toast.makeText(vContext, message, Toast.LENGTH_SHORT).show()

    }

    /**
     * Enviar postulacion a partido
     */
    fun setSendPostulacion(
        pPartidoId: String,
        pOrganizadorId: String,
        pOrganizadorToken: String,
        pUserId: String,
        pUserNombre: String,
        pUserFotoPerfil: String,
        pUserToken: String,
        pUserPosition: String,
        pSolicitudes: Int,
        pVacantes: String?
    ){

        var vProgress = ProgressDialog(vContext)
        vProgress.setMessage("Enviando solicitud")
        vProgress.show()

        var datosUser: Map<String, String> = mapOf()
        datosUser = mutableMapOf(
            "usr_id" to pUserId,
            "usr_fotoPerfil" to pUserFotoPerfil,
            "usr_nombre" to pUserNombre,
            "usr_position" to pUserPosition,
            "usr_tokenDispositivo" to pUserToken)


        val vNotificacion = hashMapOf(
            "not_partidoId" to pPartidoId,
            "not_organizadorId" to pOrganizadorId,
            "not_organizadorToken" to pOrganizadorToken,
            "not_status" to "P",
            "not_addDate" to Timestamp(Date().time),
            "datosUser" to datosUser,
            "not_vacantes" to pVacantes.toString().toInt()
        )

        db.collection("notificaciones")
            .add(vNotificacion)
            .addOnSuccessListener {
                db.collection("partidos").document(pPartidoId)
                    .update("par_solicitudes", pSolicitudes)
                    vProgress.dismiss()
                    Toast.makeText(vContext, "Te postulaste correctamente.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                vProgress.dismiss()
                Toast.makeText(vContext, "Ocurrió un error al enviar la solicitud.\nPor favor, intentá nuevamente más tarde.", Toast.LENGTH_SHORT).show()
            }



    }

    /**
     * Modificar el estado de la solicitud
     */
    fun changeStatusSolicitud(
        pNotificacionId: String,
        pStatus: String,
        pPartidoId: String,
        notifUserNombre: String,
        notifUserFotoPerfil: String,
        notifVacantes: Int,
        notifUserId: String
    ){
        var vMessage: String
        if(pStatus == "Y"){
            vMessage = "Solicitud Aceptada"
        }else{
            vMessage = "Solicitud Rechazada"
        }
        db.collection("notificaciones").document(pNotificacionId)
            .update("not_status", pStatus)
            .addOnSuccessListener {
                Toast.makeText(vContext, vMessage, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(vContext, "Ocurrió un error inesperado. Por favor, intentá más tarde.", Toast.LENGTH_SHORT).show()
            }

        db.collection("partidos").document(pPartidoId)
            .update("par_solicitudes", FieldValue.increment(-1  ))

        var vJugador: String

        if(pStatus == "Y"){

            val vVacante = mutableMapOf(
                "usr_fotoPerfil" to notifUserFotoPerfil,
                "usr_nombre" to notifUserNombre,
                "usr_id" to notifUserId
            )

            when(notifVacantes){
                1 -> {
                    db.collection("detallePartidos").document(pPartidoId)
                        .update(mapOf(
                            "dte_vacantes" to FieldValue.increment(-1),
                            "jugador4" to vVacante
                        ))
                    db.collection("partidos").document(pPartidoId)
                        .update(mapOf(
                            "par_vacantes" to FieldValue.increment(-1),
                            "par_jugador4" to notifUserId
                        ))
                }
                2 -> {
                    db.collection("detallePartidos").document(pPartidoId)
                        .update(mapOf(
                            "dte_vacantes" to FieldValue.increment(-1),
                            "jugador3" to vVacante
                        ))
                    db.collection("partidos").document(pPartidoId)
                        .update(mapOf(
                            "par_vacantes" to FieldValue.increment(-1),
                            "par_jugador3" to notifUserId
                        ))
                }
                3 -> {
                    db.collection("detallePartidos").document(pPartidoId)
                        .update(mapOf(
                            "dte_vacantes" to FieldValue.increment(-1),
                            "jugador2" to vVacante
                        ))
                    db.collection("partidos").document(pPartidoId)
                        .update(mapOf(
                            "par_vacantes" to FieldValue.increment(-1),
                            "par_jugador2" to notifUserId
                        ))
                }
                4 -> {
                    db.collection("detallePartidos").document(pPartidoId)
                        .update(mapOf(
                            "dte_vacantes" to FieldValue.increment(-1),
                            "jugador1" to vVacante
                        ))
                    db.collection("partidos").document(pPartidoId)
                        .update(mapOf(
                            "par_vacantes" to FieldValue.increment(-1),
                            "par_jugador1" to notifUserId
                        ))
                }

            }


            db.collection("notificaciones").document(pNotificacionId)
                .update("not_vacantes", FieldValue.increment(-1))

        }
    }




    /**
     * setPostTribuna
     */
    fun setPostTribuna(pUserId: String, pUserNombre: String, pUserFotoPerfil: String, pMensaje: String){

        val vPost = hashMapOf(
            "post_usrId" to pUserId,
            "post_usrNombre" to pUserNombre,
            "post_usrFotoPerfil" to pUserFotoPerfil,
            "post_message" to pMensaje,
            "post_addDate" to Timestamp(Date().time),
            "post_cantComentarios" to 0
        )

        db.collection("tribuna")
            .add(vPost)
            .addOnSuccessListener {
                Toast.makeText(vContext, "Post compartido correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(vContext, "Ocurrio un error al cargar el post", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * setComentarioPost
     */
    fun setComentarioPost(pPostId: String, pUserId: String, pUserNombre: String, pUserFotoPerfil: String, pMensaje: String, pCantComentarios: Int){

        var datosUser: Map<String, String> = mapOf()
        datosUser = mutableMapOf("usr_id" to pUserId,
            "usr_fotoPerfil" to pUserFotoPerfil,
            "usr_nombre" to pUserNombre)

        val vPost = hashMapOf(
            "com_idPost" to pPostId,
            "com_datosUser" to datosUser,
            "com_message" to pMensaje,
            "com_addDate" to Timestamp(Date().time)
        )

        db.collection("comentarios")
            .add(vPost)
            .addOnSuccessListener {
                db.collection("tribuna").document(pPostId)
                    .update("post_cantComentarios", pCantComentarios+1)
                    .addOnSuccessListener {
                        Toast.makeText(vContext, "Agregaste un comentario al post", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(vContext, "Ocurrio un error al cargar el comentario", Toast.LENGTH_SHORT).show()
            }
    }


}