package com.example.examen

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class PlayerList : AppCompatActivity() {
    private lateinit var adaptador: ArrayAdapter<DPlayer>
    private lateinit var players : ArrayList<DPlayer>
    private var selectedItem = -1
    private var currentId: Int = -1

    private val callBackIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            updaAdapter()
            if (result.data != null) {
                val data = result.data
                showSnackBar("${data?.getStringExtra("action")}")
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.child_object_menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        selectedItem = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_p -> {
                openForm(players[selectedItem].id)
                return true
            }

            R.id.delete_p -> {
                deleteDialog(players[selectedItem].id)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun deleteDialog(item: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            SqlDataBase.tables?.delete(item,"players")
            updaAdapter()
            showSnackBar("Eliminar aceptado")
        })
        builder.setNegativeButton(
            "Cancelar", null
        )
        val dialogo = builder.create()
        dialogo.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_list)

        val team = intent.getStringExtra("team_name")
        val title = findViewById<TextView>(R.id.child_title)
        title.text = team

        val listView = findViewById<ListView>(R.id.lv_player)
        val teamId = intent.getIntExtra("team", -1)
        currentId = teamId
        players= SqlDataBase.tables?.retrieveByTeam(currentId)!!

        this.adaptador = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, players
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)

        val botonAnadirListView = findViewById<Button>(
            R.id.create_p
        )
        botonAnadirListView.setOnClickListener {
            val newId = SqlDataBase.tables!!.getMaxId("players") + 1
            openForm(newId)
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            finish()
        }
    }



    private fun openForm(playerId: Int) {
        val explicitIntent = Intent(this, PlayerForm::class.java);
        explicitIntent.putExtra("player_id", playerId)
        explicitIntent.putExtra("team_id", currentId)
        callBackIntent.launch(explicitIntent)
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(
            findViewById(R.id.lv_player), text, Snackbar.LENGTH_LONG
        ).show()
    }

    private fun updaAdapter(){
        players = SqlDataBase.tables?.retrieveByTeam(currentId)!!
        Log.d("UPDATING ADAPTER PLAYER", players.toString())
        adaptador.clear()
        players.forEach {
            adaptador.insert(it, adaptador.count)
        }
    }
}