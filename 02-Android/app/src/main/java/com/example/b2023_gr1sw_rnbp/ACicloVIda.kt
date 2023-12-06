package com.example.b2023_gr1sw_rnbp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.google.android.material.snackbar.Snackbar

class ACicloVIda : AppCompatActivity() {
    var textoGlobal = ""
    fun mostrarSnackbar(texto: String) {
        textoGlobal = textoGlobal + " " + texto;
        Snackbar.make(
            findViewById(R.id.cl_aciclo_vida),
            textoGlobal,
            Snackbar.LENGTH_INDEFINITE
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aciclo_vida)
        mostrarSnackbar("OnCreate")
    }

    override fun onStart() {
        super.onStart()
        mostrarSnackbar("OnStart")
    }

    override fun onResume() {
        super.onResume()
        mostrarSnackbar("OnResume")
    }

    override fun onRestart() {
        super.onRestart()
        mostrarSnackbar("OnRestart")
    }

    override fun onPause() {
        super.onPause()
        mostrarSnackbar("OnPause")
    }

    override fun onStop() {
        super.onStop()
        mostrarSnackbar("OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        mostrarSnackbar("OnDestroy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aciclo_vida)
    }
}