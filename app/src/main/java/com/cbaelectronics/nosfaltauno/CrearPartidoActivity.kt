/*
 * Created by Cba Electronics on 18/08/20 17:27
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 18/08/20 17:27
 */

package com.cbaelectronics.nosfaltauno

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cbaelectronics.nosfaltauno.Model.dataNotification
import com.cbaelectronics.nosfaltauno.dataModel.firebase.setFirestore
import com.cbaelectronics.nosfaltauno.vo.RetrofitClient
import kotlinx.android.synthetic.main.activity_crear_partido.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class CrearPartidoActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private val key                 = "MY_KEY"
    private val usrId               = "MY_ID"
    private val usrTokenDispositivo = "MY_TOKEN"
    private val usrNombre           = "MY_NOMBRE"
    private val usrEmail            = "MY_EMAIL"
    private val usrFotoPerfil       = "MY_FOTOPERFIL"
    private val usrCategory         = "MY_CATEGORY"
    private val usrPosition         = "MY_POSITION"

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var vDay = 0
    var vMonth = 0
    var vYear = 0
    var vHour = 0
    var vMinute = 0

    val setFirebase =
        setFirestore(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_partido)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.title = Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.title_activity_crearPartido) + "</font>")

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val arrayCategory = resources.getStringArray(R.array.strCategory)
        val arrayTecho = resources.getStringArray(R.array.tipoTecho)
        val arrayPiso = resources.getStringArray(R.array.tipoPiso)
        val arrayPared = resources.getStringArray(R.array.tipoPared)
        val arrayGenero = resources.getStringArray(R.array.generoPartido)

        val adapterCategory = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayCategory
        )

        val adapterTecho = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayTecho
        )

        val adapterPiso = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayPiso
        )

        val adapterPared = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayPared
        )

        val adapterGenero = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayGenero
        )

        //filled_exposed_dropdown.setText(adapter.getItem(0), false)
        spinnerCategory.setAdapter(adapterCategory)
        spinnerTecho.setAdapter(adapterTecho)
        spinnerPiso.setAdapter(adapterPiso)
        spinnerPared.setAdapter(adapterPared)
        spinnerGenero.setAdapter(adapterGenero)

        pickDate()
        hideKeyboard()


        btnOrganizarPartido.setOnClickListener {
            if (vComplejo.text.isNullOrEmpty() ||
                vFechaPartido.text.isNullOrEmpty() ||
                vVacantes.text.isNullOrEmpty() ||
                spinnerCategory.text.isNullOrEmpty() ||
                spinnerGenero.text.isNullOrEmpty() ||
                spinnerTecho.text.isNullOrEmpty() ||
                spinnerPiso.text.isNullOrEmpty() ||
                spinnerPared.text.isNullOrEmpty()){

                val vMensaje = "Debes completar todos los datos para organizar un partido"
                mostrarAlert(vMensaje)
                //Toast.makeText(this, "Debes completar todos los datos para organizar un partido", Toast.LENGTH_SHORT).show()

            }else if (vVacantes.text.toString().toInt() > 4){
                val vMensaje = "La cantidad de vacantes no puede ser mayor que 4"
                mostrarAlert(vMensaje)
            }
            else{
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.parse(vFechaPartido.text.toString())
                setFirebase.setPartido(
                    prefs.getString(usrId, "DEFAULT").toString(),
                    vNombrePartido.text.toString(),
                    vComplejo.text.toString(),
                    date,
                    vVacantes.text.toString().toInt(),
                    spinnerCategory.text.toString(),
                    spinnerGenero.text.toString(),
                    spinnerTecho.text.toString(),
                    spinnerPiso.text.toString(),
                    spinnerPared.text.toString(),
                    prefs.getString(usrFotoPerfil, "DEFAULT").toString(),
                    prefs.getString(usrNombre, "DEFAULT").toString(),
                    prefs.getString(usrTokenDispositivo, "DEFAULT").toString()
                )

                startActivity(Intent(this, HomeActivity::class.java))
                finish()

            }

        }

    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        // else {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        // }
    }

    private fun mostrarAlert(pMensaje: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Error")
        dialog.setMessage(pMensaje)
        dialog.setCancelable(false)
        dialog.setPositiveButton("OK", null)
        dialog.show()
    }

    private fun getDateTimeCalendar(){

        TimeZone.getDefault()
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }

    private fun pickDate(){

        vFechaPartido.setOnFocusChangeListener { view, b ->

            if (b == true){
                getDateTimeCalendar()
                DatePickerDialog(this, this, year, month, day).show()
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        vYear = p1
        vMonth = p2+1
        vDay = p3

        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        vHour = p1
        vMinute = p2

        val vFecha = "$vDay/$vMonth/$vYear $vHour:$vMinute"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = sdf.parse(vFecha)
        val newDate = sdf.format(date)

        vFechaPartido.setText(newDate)
    }

}