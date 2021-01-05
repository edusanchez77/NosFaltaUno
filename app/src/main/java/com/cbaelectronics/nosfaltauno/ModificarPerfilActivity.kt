/**
 * Created by Cba Electronics on 13/08/20 11:45
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 13/08/20 11:45
 */

package com.cbaelectronics.nosfaltauno

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isNotEmpty
import com.bumptech.glide.Glide
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_modificar_perfil.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_help1.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.Int as Int1

class ModificarPerfilActivity : AppCompatActivity() {

    private val key             = "MY_KEY"
    private val usrId           = "MY_ID"
    private val usrNombre       = "MY_NOMBRE"
    private val usrEmail        = "MY_EMAIL"
    private val usrFotoPerfil   = "MY_FOTOPERFIL"
    private val usrCategory     = "MY_CATEGORY"
    private val usrPosition     = "MY_POSITION"

    val REQUEST_IMAGE_CAPTURE = 1
    val IMAGE_PICK_COODE = 1000

    val setFirebase =
        setFirestore(this)
    var url = ""
    var vUrlImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_perfil)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arrayCategory = resources.getStringArray(R.array.strCategory)
        val arrayPosition = resources.getStringArray(R.array.strPosition)

        val adapterCategory = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayCategory
        )

        val adapterPosition = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayPosition
        )

        spinnerCategoryModifPerfil.setAdapter(adapterCategory)
        spinnerPositionModifPerfil.setAdapter(adapterPosition)


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var vFlagImagenPerfil = false

        Glide.with(this).load(prefs.getString(usrFotoPerfil, "DEFAULT")).into(circleImageView)

        nombreModifPerfil.hint = prefs.getString(usrNombre, "DEFAULT")
        //emailModifPerfil.hint = prefs.getString(usrEmail, "DEFAULT")
        //passModifPerfil.hint = "******"
        spinnerPositionModifPerfil.hint = prefs.getString(usrPosition, "DEFAULT")
        spinnerCategoryModifPerfil.hint = prefs.getString(usrCategory, "DEFAULT")

        circleImageView.setOnClickListener {
            var alert = AlertDialog.Builder(this)
                .setTitle("Elige una opción")
              .setItems(R.array.opcionesCamara, DialogInterface.OnClickListener { dialogInterface, i ->
                    when(i){
                        0 -> {
                            vFlagImagenPerfil = dispatchTakePictureIntent()
                        }
                        1 -> {
                            vFlagImagenPerfil = pickImageFromGallery()
                        }
                    }
                })

            alert.show()
        }


        btnGuardarPerfil.setOnClickListener {

            var vPosition: String = ""

            /*if (vFlagImagenPerfil == true){
                val storageRef = Firebase.storage.reference.child("fotoPerfil/"+prefs.getString(usrId, "DEFAULT"))


                circleImageView.isDrawingCacheEnabled = true
                circleImageView.buildDrawingCache()

                val bitmap = (circleImageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                val data = baos.toByteArray()

                var uploadTask = storageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    Toast.makeText(this, "SUBIDO ERROR", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    url = storageRef.downloadUrl.toString()
                    Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
                }
            }*/




            if (nombreModifPerfil.text.isNullOrEmpty() &&
                spinnerPositionModifPerfil.text.isNullOrBlank() &&
                spinnerCategoryModifPerfil.text.isNullOrBlank() &&
                vUrlImage.isNullOrEmpty()){

                Toast.makeText(this, "No se modificó ninguno de los datos", Toast.LENGTH_SHORT).show()

            }else{

                setFirebase.updateUser(
                    prefs.getString(usrId, "DEFAULT").toString(),
                    nombreModifPerfil.text.toString(),
                    //emailModifPerfil.text.toString(),
                    vUrlImage,
                    spinnerCategoryModifPerfil.text.toString(),
                    spinnerPositionModifPerfil.text.toString())

                if(!nombreModifPerfil.text.isNullOrEmpty()){
                    prefs.edit()
                        .remove(usrNombre)
                        .putString(usrNombre, nombreModifPerfil.text.toString())
                        .apply()
                }
                if(!spinnerCategoryModifPerfil.text.isNullOrEmpty()){
                    prefs.edit()
                        .putString(usrCategory, spinnerCategoryModifPerfil.text.toString())
                        .apply()
                }
                if(!spinnerPositionModifPerfil.text.isNullOrEmpty()){
                    prefs.edit()
                        .putString(usrPosition, spinnerPositionModifPerfil.text.toString())
                        .apply()
                }
                if(!vUrlImage.isNullOrEmpty()){
                    prefs.edit()
                        .putString(usrFotoPerfil, vUrlImage)
                        .apply()
                }

            }


        }



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    private fun dispatchTakePictureIntent(): Boolean {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
        return true
    }

    private fun pickImageFromGallery(): Boolean{
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_COODE)

        return true
    }

    override fun onActivityResult(requestCode: Int1, resultCode: Int1, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val vProgress = ProgressDialog(this)
        vProgress.setMessage("Cargando imagen")
        vProgress.show()

        //Gallery
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_COODE){


            var urlImage = data?.data?.path
            var file = Uri.fromFile(File(urlImage))
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)

            val storageRef = Firebase.storage.reference.child("fotoPerfil/"+prefs.getString(usrId, "DEFAULT"))

            Glide.with(this).load(data?.data).into(circleImageView)


            if (data?.data != null){
                storageRef.putFile(data.data!!)
                    .addOnSuccessListener {

                        storageRef.downloadUrl.addOnSuccessListener {
                            //Toast.makeText(this, it.path, Toast.LENGTH_SHORT).show()
                            vUrlImage = it.toString()
                            vProgress.dismiss()
                        }
                    }
                    .addOnFailureListener{
                        vProgress.dismiss()
                        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                    }
            }


        }

        //Camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            circleImageView.setImageBitmap(imageBitmap)


            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val storageRef = Firebase.storage.reference.child("fotoPerfil/"+prefs.getString(usrId, "DEFAULT"))


            // Get the data from an ImageView as bytes
            circleImageView.isDrawingCacheEnabled = true
            circleImageView.buildDrawingCache()
            val bitmap = (circleImageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = storageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                vProgress.dismiss()
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    //Toast.makeText(this, it.path, Toast.LENGTH_SHORT).show()
                    vUrlImage = it.toString()
                    vProgress.dismiss()
                }
            }
        }
    }
}