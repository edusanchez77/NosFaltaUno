package com.cbaelectronics.nosfaltauno

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.nav_header_main.view.*


class HomeActivity : AppCompatActivity() {

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    val user = Firebase.auth.currentUser
    val db = FirebaseFirestore.getInstance()

    private val INTERVALO = 2000 //2 segundos para salir
    private var tiempoPrimerClick: Long = 0

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        subscriberToTopic()


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_partidos, R.id.nav_jugadores, R.id.nav_jugar, R.id.nav_tribuna
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val hview = navView.getHeaderView(0)
        var vUserNombre = ""
        var vUserEmail = ""
        var vImagen = ""
        var vUserCategory = ""
        var vUserPosition = ""
        
        // Obtengo datos del usuario
            db.collection("user").document(user?.uid.toString())
                .get()
                .addOnSuccessListener {
                    if(it.exists()){
                        vUserNombre = it.getString("usr_nombre").toString()
                        vUserEmail = it.getString("usr_email").toString()
                        vUserCategory = it.getString("usr_category").toString()
                        vUserPosition = it.getString("usr_position").toString()
                        vImagen = it.getString("usr_fotoPerfil").toString()

                        val editor = prefs.edit()
                        editor.putString(key, "Y")
                        editor.putString(usrId, user?.uid.toString())
                        editor.putString(usrTokenDispositivo, it.getString("usr_tokenDispositivo"))
                        editor.putString(usrNombre, it.getString("usr_nombre"))
                        editor.putString(usrEmail, it.getString("usr_email"))
                        editor.putString(usrFotoPerfil, vImagen)
                        editor.putString(usrCategory, vUserCategory)
                        editor.putString(usrPosition, vUserPosition)
                        editor.apply()

                    }else{
                        vUserNombre = "No existe"
                        vUserEmail = "No existe"
                        vImagen = "No existe"
                    }

                    mostrarDatos(
                        hview,
                        prefs.getString(usrNombre, "DEFAULT").toString(),
                        prefs.getString(usrEmail, "DEFAULT").toString(),
                        prefs.getString(usrFotoPerfil, "DEFAULT").toString()
                    )
                }
    }


    private fun subscriberToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("newPartido")
    }


    private fun mostrarDatos(
        hview: View,
        vUserNombre: String,
        vUserEmail: String,
        vUserFoto: String
    ) {
        hview.nombreHeader.text = vUserNombre
        hview.emailHeader.text = vUserEmail
        Glide.with(this).load(vUserFoto).into(hview.fotoPerfil);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_cerrarSesion -> {

                var alert = AlertDialog.Builder(this)
                alert.setMessage("¿Desea cerrar sesión?")
                    .setCancelable(false)
                    .setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = prefs.edit()
                        editor.remove(key)
                        editor.remove(usrId)
                        editor.remove(usrNombre)
                        editor.remove(usrEmail)
                        editor.remove(usrFotoPerfil)
                        editor.remove(usrCategory)
                        editor.remove(usrPosition)
                        editor.apply()

                        FirebaseAuth.getInstance().signOut()
                        db.terminate()

                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    })

                alert.show()
            }

            /*R.id.action_settings -> {
                //startActivity(Intent(this, SettingsActivity::class.java))
                startActivity(Intent(this, SwipeActivity::class.java))
            }*/

            R.id.action_ayuda -> {
                startActivity(Intent(this, HelpActivity::class.java))
            }

            /*R.id.action_contacto -> {
                startActivity(Intent(this, ContactoActivity::class.java))
            }*/

            R.id.action_modificar_perfil -> {
                startActivity(Intent(this, ModificarPerfilActivity::class.java))
                //finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            finish()
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

}