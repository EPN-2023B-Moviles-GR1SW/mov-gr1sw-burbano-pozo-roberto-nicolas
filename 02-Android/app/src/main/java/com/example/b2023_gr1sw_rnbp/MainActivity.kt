package com.example.b2023_gr1sw_rnbp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val botonCicloVida=findViewById<Button>(R.id.btn_ciclo_vida)
        boton.CicloVida.setOnClickListener{
            irActividad(ACicloVIda::class.java);
        }
    }
    fun irActividad(
        clase:Class<T>
    ){
        val itent=Intent(this,clase)
        startActivity(intent)
    }
}