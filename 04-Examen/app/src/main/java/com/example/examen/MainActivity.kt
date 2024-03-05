package com.example.examen

import android.app.Activity
import android.app.AlertDialog
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
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val teams: ArrayList<DTeam> = arrayListOf()
    private var selectedItem = -1
    var query: Query? = null
    private lateinit var adaptador: ArrayAdapter<DTeam>

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_object_menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position
        selectedItem = position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_t -> {
                Log.e("CONTEXT MENU", teams[selectedItem].id)
                openForm(teams[selectedItem].id)
                return true
            }

            R.id.delete_t -> {
                deleteDialog(teams[selectedItem].id)
                return true
            }

            R.id.view_t -> {
                val explicitIntent = Intent(this, PlayerList::class.java)
                val team = teams[selectedItem]
                explicitIntent.putExtra("team", team.id)
                explicitIntent.putExtra("team_name", team.name)
                startActivity(explicitIntent)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private val callBackIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val data = result.data
                showSnackBar("${data?.getStringExtra("action")}")
            }
            updateAdapter()
        }
    }

    private fun retrieveAllTeams(adapter: ArrayAdapter<DTeam>) {
        val db = Firebase.firestore
        val citiesRef = db.collection("teams")
        citiesRef.get()
                .addOnSuccessListener { documentSnapshots ->
                    guardarQuery(documentSnapshots, citiesRef)
                    for (team in documentSnapshots) {
                        Log.e("Firebase function", team.toString())
                        anadirAArregloTeam(team)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {/* si hay fallos*/ }
    }

    fun anadirAArregloTeam(
        document: QueryDocumentSnapshot
    ) {
        val newTeam = DTeam(
            document.id,
            document.data["name"] as String,
            document.data["foundationDate"] as String,
            document.data["netIncome"] as Long,
            document.data["isActive"] as Boolean,
        )
        teams.add(newTeam)
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

    private fun updateAdapter() {
        adaptador.clear()
        retrieveAllTeams(this.adaptador)
        teams.forEach {
           adaptador.insert(it, adaptador.count)
        }
    }

    private fun deleteDialog(item: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val db = Firebase.firestore
            val referenciaEjemploEstudiante = db.collection("teams")
            referenciaEjemploEstudiante
                .document(item)
                .delete()
                .addOnCompleteListener {updateAdapter()}
                .addOnFailureListener {}
            showSnackBar("Eliminar aceptado")
        }
        builder.setNegativeButton(
            "Cancelar", null
        )
        val dialog = builder.create()
        dialog.show()
    }


    private fun showSnackBar(text: String) {
        Snackbar.make(
            findViewById(R.id.lv_player), text, Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.lv_player)
        this.adaptador = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, teams
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        Log.e("Firebase",teams.toString())
        retrieveAllTeams(adaptador)
        Log.e("Firebase",teams.toString())

        val buttonAddListView = findViewById<Button>(
            R.id.create_p
        )
        buttonAddListView.setOnClickListener {
            openForm("2")
        }
        registerForContextMenu(listView)
    }

    private fun openForm(teamId: String?) {
        val explicitIntent = Intent(this, TeamForm::class.java)
        if (teamId != null) {
            explicitIntent.putExtra("team_id", teamId)
        }
        callBackIntent.launch(explicitIntent)
    }
}

