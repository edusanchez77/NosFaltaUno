/*
 * Created by Cba Electronics on 2/10/20 15:26
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cbaelectronics.nosfaltauno.adapters.loginAdapter
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.login_tab_fragment.*
import kotlinx.android.synthetic.main.login_tab_fragment.btnOlvidePassword
import kotlinx.android.synthetic.main.login_tab_fragment.view.*

class AuthActivity : AppCompatActivity() {

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    private lateinit var auth: FirebaseAuth
    val setFirebase =
        setFirestore(this)
    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()
    private var tokenDispositivo = ""

    private val adapter by lazy { loginAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Obtengo el token del dispositivo para enviar las notificaciones
        tokenDispositivo = getTokenDispositivo()

        //Tabs
        viewPagerLogin.adapter = adapter
        val tabLayoutMediator = TabLayoutMediator(tabLayoutLogin, viewPagerLogin,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0 -> {
                        tab.text = "LOGIN"
                    }
                    1 -> {
                        tab.text = "REGISTRO"
                    }
                }
            })
        tabLayoutMediator.attach()



        //Inicio de sesion con Google
        fabGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }


        //Inicio de sesion con Facebook
        fabFacebook.setOnClickListener {

            LoginManager.getInstance().logOut()
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult>{

                    override fun onSuccess(result: LoginResult?) {
                        val vProgress = ProgressDialog(this@AuthActivity)
                        vProgress.setMessage("Iniciando sesión")
                        vProgress.show()
                        result?.let {
                            val token = it.accessToken

                            val credential = FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener {
                                    if (it.isSuccessful){
                                        val datosUser = Firebase.auth.currentUser
                                        var firestone = FirebaseFirestore.getInstance()
                                        var homeActivity = Intent(this@AuthActivity, HomeActivity::class.java)
                                        var completeRegisterActivity = Intent(this@AuthActivity, CompleteRegisterActivity::class.java)

                                        val docRef = firestone.collection("user").document(datosUser?.uid.toString())
                                        docRef.get()
                                            .addOnSuccessListener { document ->
                                                if (document.exists()) {

                                                    val prefs = PreferenceManager.getDefaultSharedPreferences(this@AuthActivity)
                                                    val editor = prefs.edit()
                                                    editor.putString(key, "Y")
                                                    editor.apply()

                                                    startActivity(homeActivity)
                                                    //finish();
                                                }else{
                                                    setFirebase.setUser(
                                                        datosUser?.uid.toString(),
                                                        datosUser?.displayName.toString(),
                                                        apellidoRegistro?.text.toString(),
                                                        datosUser?.email.toString(),
                                                        datosUser?.photoUrl.toString(),
                                                        "No Definida",
                                                        "No Definida",
                                                        "GOOGLE",
                                                        tokenDispositivo)

                                                    vProgress.dismiss()
                                                    startActivity(completeRegisterActivity)
                                                    //finish();
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                vProgress.dismiss()
                                                val mMessage = "Estamos presentando problemas en nuestros servidores. Por favor intentá más tarde"
                                                showAlert(mMessage)
                                                //Toast.makeText(this, "ERROR addOnFailureListener", Toast.LENGTH_LONG).show()
                                            }

                                    }else{
                                        vProgress.dismiss()
                                        val mMessage = "Estamos presentando problemas en nuestros servidores. Por favor intentá más tarde"
                                        showAlert(mMessage)
                                        //Toast.makeText(this, "Estamos presentando problemas en ", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }

                    override fun onCancel() {
                    }

                    override fun onError(p0: FacebookException?) {
                        showAlert("No se ha podido iniciar el login con facebook")
                    }
                })
        }
    }


    private fun getTokenDispositivo(): String {

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            it.result?.token?.let {
                tokenDispositivo = it
            }
        }

        return tokenDispositivo
    }

    private fun showHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
        //finish()
    }

    private fun showAlert(mMessage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mMessage)
        builder.setPositiveButton("Aceptar", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        val vProgress = ProgressDialog(this)

        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            vProgress.setMessage("Iniciando sesión")
            vProgress.show()

            try {
                val account = task.getResult(ApiException::class.java)

                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val datosUser = Firebase.auth.currentUser
                                var firestone = FirebaseFirestore.getInstance()
                                var homeActivity = Intent(this, HomeActivity::class.java)
                                var completeRegisterActivity = Intent(this, CompleteRegisterActivity::class.java)

                                val docRef = firestone.collection("user").document(datosUser?.uid.toString())
                                docRef.get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {

                                            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                                            val editor = prefs.edit()
                                            editor.putString(key, "Y")
                                            editor.apply()

                                            startActivity(homeActivity)
                                            //finish();
                                        }else{
                                            setFirebase.setUser(
                                                datosUser?.uid.toString(),
                                                datosUser?.displayName.toString(),
                                                apellidoRegistro?.text.toString(),
                                                datosUser?.email.toString(),
                                                datosUser?.photoUrl.toString(),
                                                "No Definida",
                                                "No Definida",
                                                "GOOGLE",
                                                tokenDispositivo)

                                            vProgress.dismiss()
                                            startActivity(completeRegisterActivity)
                                            //finish();
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        vProgress.dismiss()
                                        val mMessage = "Estamos presentando problemas en nuestros servidores. Por favor intentá más tarde"
                                        showAlert(mMessage)
                                        //Toast.makeText(this, "ERROR addOnFailureListener", Toast.LENGTH_LONG).show()
                                    }

                            }else{
                                vProgress.dismiss()
                                val mMessage = "Estamos presentando problemas en nuestros servidores. Por favor intentá más tarde"
                                showAlert(mMessage)
                                //Toast.makeText(this, "Estamos presentando problemas en ", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }catch (e: ApiException){
                vProgress.dismiss()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

        }
    }
}