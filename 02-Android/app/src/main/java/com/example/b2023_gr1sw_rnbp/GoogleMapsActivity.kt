package com.example.b2023_gr1sw_rnbp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class GoogleMapsActivity : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    var permisos = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)
        solicitarPermisos()
    }


    fun iniciarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
// with(X) => if(X != null)
            with(googleMap) {
                mapa = googleMap
                establecerConfiguracionMapa()
            }
        }
    }

    fun establecerConfiguracionMapa() {
        val contexto = this.applicationContext
        with(mapa) {
            val permisosFineLocation = ContextCompat
                .checkSelfPermission(
                    contexto,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            val tienePermisos = permisosFineLocation == PackageManager
                .PERMISSION_GRANTED
            if (tienePermisos) {
                mapa.isMyLocationEnabled = true // tenemos permisos
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true // Defecto
        }
    }

    fun solicitarPermisos() {
        val contexto = this.applicationContext
        val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
        val permisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val permisosFineLocation = ContextCompat
            .checkSelfPermission(
                contexto, // permiso que van a checkear
                permisoFineLocation
            )
        val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
        if (tienePermisos) {
            permisos = true
        } else {
            ActivityCompat.requestPermissions(
                this, // Contexto
                arrayOf( // Arreglo Permisos
                    permisoFineLocation, permisoCoarse
                ),
                1 // Codigo de peticion de los permisos
            )
        }
    }
}