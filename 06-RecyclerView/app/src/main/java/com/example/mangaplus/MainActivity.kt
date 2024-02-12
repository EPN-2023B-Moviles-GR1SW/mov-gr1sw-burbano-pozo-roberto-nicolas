package com.example.mangaplus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView
import com.example.mangaplus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MemoryDataBase()
        initRecyclerView()
        val btnShelf = findViewById<ImageButton>(R.id.btn_nav_shelf)
        btnShelf.setOnClickListener {
            irActividad(Shelf::class.java)
        }

    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_animes)
        val adaptador = AnimeAdapter(
            this, // Contexto
            MemoryDataBase.arrayAnimes, // Arreglo datos
            recyclerView // Recycler view
        )
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget
            .DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget
            .GridLayoutManager(this,3)
        adaptador.notifyDataSetChanged()
    }


}