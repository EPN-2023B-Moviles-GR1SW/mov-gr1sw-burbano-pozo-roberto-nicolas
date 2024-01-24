package com.example.b2023_gr1sw_rnbp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.b2023_gr1sw_vaes.EBaseDeDatos
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Base de datos sqlite
        EBaseDeDatos.tablaEntrenador = ESqliteHelperEntrenador(this)

        setContentView(R.layout.activity_main)
        val botonCicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        botonCicloVida.setOnClickListener {
            irActividad(ACicloVIda::class.java);
        }
        val botonListView = findViewById<Button>(R.id.btn_listView)
        botonListView.setOnClickListener {
            irActividad(BListView::class.java);
        }

        val botonIntentImplicito = findViewById<Button>(
            R.id.btn_ir_intent_implicito
        )
        botonIntentImplicito
            .setOnClickListener {
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                callbackIntentPickUri.launch(intentConRespuesta)
            }
        val botonIntentExplicito = findViewById<Button>(
            R.id.btn_ir_intent_explicito
        )
        botonIntentExplicito
            .setOnClickListener {
                abrirActividadconParametros(
                    CIntentExplicitoParametros::class.java
                )
            }
        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite.setOnClickListener {
            irActividad(ECrudEntrenador::class.java)
        }
        val botonRecicler = findViewById<Button>(R.id.btn_revcycler_view)
        botonRecicler.setOnClickListener {
            irActividad(ECrudEntrenador::class.java)
        }
        val botonGoogle = findViewById<Button>(R.id.btn_google_maps)
        botonGoogle.setOnClickListener {
            irActividad(GoogleMapsActivity::class.java)
        }

        val firebase = findViewById<Button>(R.id.btn_intent_firebase_ui)
        firebase.setOnClickListener {
            irActividad(HFirebaseUIAuth::class.java)
        }
    }

    fun abrirActividadconParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        // Enviar parametros (solamente variables primitivas)
        intentExplicito.putExtra("nombre", "Nicolas")
        intentExplicito.putExtra("apellido", "Burbano")
        intentExplicito.putExtra("edad", 24)
        callbackContenidoIntentExplicito.launch(intentExplicito)
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val itent = Intent(this, clase)
        startActivity(intent)
    }

    val callbackContenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                // Logica del negocio
                val data = result.data
                mostrarSnackbar("${data?.getStringExtra("nombreModificado")}")
            }
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.ly_main), texto, Snackbar.LENGTH_LONG).show()
    }

    val callbackIntentPickUri = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode === Activity.RESULT_OK) {
            if (result.data != null) {
                if (result.data!!.data != null) {
                    val uri: Uri = result.data!!.data!!
                    val cursor = contentResolver.query(
                        uri,
                        null, null, null, null, null
                    )
                    cursor?.moveToFirst()
                    val indiceTelefono =
                        cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val telefono = cursor?.getString(indiceTelefono!!)
                    cursor?.close()
                    mostrarSnackbar("Telefono${telefono}")
                }
            }
        }
    }
}