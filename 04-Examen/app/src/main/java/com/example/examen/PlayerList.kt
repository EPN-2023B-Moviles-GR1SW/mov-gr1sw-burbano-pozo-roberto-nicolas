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
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PlayerList : AppCompatActivity() {
    private lateinit var adaptador: ArrayAdapter<DPlayer>
    private var selectedItem = -1
    private var currentId: String = ""
    var query: Query? = null
    val players: ArrayList<DPlayer> = arrayListOf()


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

    private fun deleteDialog(item: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val db = Firebase.firestore
            val referenciaEjemploEstudiante = db.collection("players")
            referenciaEjemploEstudiante
                .document(item)
                .delete()
                .addOnCompleteListener {updaAdapter()}
                .addOnFailureListener {}
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
        if (team != null) {
            currentId = team as String
        }

        this.adaptador = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, players
        )
        listView.adapter = adaptador

        retrieveAll(adaptador, currentId)

        registerForContextMenu(listView)

        val botonAnadirListView = findViewById<Button>(
            R.id.create_p
        )
        botonAnadirListView.setOnClickListener {
            openForm(null)
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun retrieveAll(adapter: ArrayAdapter<DPlayer>, team: String) {
        val db = Firebase.firestore
        val citiesRef = db.collection("players")
        citiesRef.whereEqualTo("team", team).get()
            .addOnSuccessListener { documentSnapshots ->
                guardarQuery(documentSnapshots, citiesRef)
                for (player in documentSnapshots) {
                    Log.e("Firebase function", player.toString())
                    addArray(player)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {/* si hay fallos*/ }
    }

    fun addArray(
        document: QueryDocumentSnapshot
    ) {
        val newTeam = DPlayer(
            document.id,
            document.data["debutDate"] as String,
            document.data["isInjured"] as Boolean,
            document.data["name"] as String,
            document.data["value"] as Long,
            document.data["team"] as String,
        )
        players.add(newTeam)
    }

    fun guardarQuery(
        documentSnapshots: QuerySnapshot,
        refCities: Query
    ) {
        if (documentSnapshots.size() > 0) {
            val ultimoDocumento = documentSnapshots
                .documents[documentSnapshots.size() - 1]
            query = refCities
                .startAfter(ultimoDocumento)
        }
    }

    private fun openForm(playerId: String?) {
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

    private fun updaAdapter() {
        adaptador.clear()
        retrieveAll(adaptador, currentId)
    }



}