package com.cbaelectronics.nosfaltauno

import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

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
    private var tokenDispositivo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //auth = Firebase.auth
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)


        //Toast.makeText(this, prefs.getString(key, "N"), Toast.LENGTH_SHORT).show()

        /*if(prefs.getString(key, "N") == "Y" ){
            showHome()
            finish()
        }*/

        //Obtengo el token del dispositivo para enviar las notificaciones
        tokenDispositivo = getTokenDispositivo()

        btnLogin.setOnClickListener {
            val vProgress = ProgressDialog(this)
            if(emailLogin.text.toString().isNotEmpty() && passLogin.text.toString().isNotEmpty()){

                vProgress.setMessage("Iniciando sesión")
                vProgress.show()

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        emailLogin.text.toString(),
                        passLogin.text.toString()
                    )
                    .addOnCompleteListener(){
                        if (it.isSuccessful){

                            val editor = prefs.edit()
                            editor.putString(key, "Y")
                            editor.apply()

                            vProgress.dismiss()
                            showHome()
                        }else{
                            vProgress.dismiss()
                            val mMessage = "Se ha producido un error autenticando al usuario"
                            showAlert(mMessage)
                        }
                    }
            }else{
                val mMessage = "Se ha producido un error autenticando al usuario"
                showAlert(mMessage)
            }
        }

        btnGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }

        btnOlvidePassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        btnRegistro.setOnClickListener{
            startActivity(
                Intent(this, RegisterActivity::class.java)
                    .putExtra("tokenDispositivo", tokenDispositivo)
            )
        }

        ayuda.setOnClickListener {
            val HelpActivity = Intent(this, HelpActivity::class.java)
            val logoView = findViewById<View>(R.id.logo)
            var pair = Pair<View, String>(logoView, "imageTransition")

            val options = ActivityOptions
                .makeSceneTransitionAnimation(this, pair)

            startActivity(HelpActivity, options.toBundle())
        }

        acercaDe.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_popup, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            mBuilder.show()
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
                val mMessage = "Estamos presentando problemas en nuestros servidores. Por favor intentá más tarde"
                showAlert(mMessage)
            }

        }
    }
}