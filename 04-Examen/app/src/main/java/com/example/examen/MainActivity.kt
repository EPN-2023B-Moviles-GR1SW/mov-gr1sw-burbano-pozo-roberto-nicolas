package com.example.examen

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val list = MemoryDataBase.teams
    var selectedItem = -1
    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_object_menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        selectedItem = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_t -> {
                mostrarSnackbar("${selectedItem}")
                //TODO ADD NAVIGATION
                return true
            }

            R.id.delete_t -> {
                mostrarSnackbar("${selectedItem}")
                abrirDialogo()
                return true
            }

            R.id.view_t -> {
                mostrarSnackbar("${selectedItem}")
                //TODO ADD NAVIGATION
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            mostrarSnackbar("Eliminar aceptado")
        })
        builder.setNegativeButton(
            "Cancelar", null
        )
        val dialogo = builder.create()
        dialogo.show()
    }


    fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.lv_team), // view
            texto, // texto
            Snackbar.LENGTH_LONG // tiempo
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv_team)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            list
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val botonAnadirListView = findViewById<Button>(
            R.id.create_t
        )
        botonAnadirListView
            .setOnClickListener {
                anadirEntrenador(adaptador)
            }
        registerForContextMenu(listView)
    }

    fun anadirEntrenador(
        adaptador: ArrayAdapter<DTeam>
    ) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        list.add(
            DTeam(5, "Olmedo", dateFormatter.parse("1960-01-02"), 249.3F, true),
            )
        adaptador.notifyDataSetChanged()
    }
}