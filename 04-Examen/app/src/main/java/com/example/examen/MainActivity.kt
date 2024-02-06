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
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var adaptador: ArrayAdapter<DTeam>
    private lateinit var teams: ArrayList<DTeam>
    private var selectedItem = -1
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
            updaAdapter()
            adaptador.notifyDataSetChanged()

        }
    }

    private fun updaAdapter() {
        teams = SqlDataBase.tables!!.retrieveAllTeams()
        adaptador.clear()
        teams.forEach {
            adaptador.insert(it, adaptador.count)
        }
    }

    private fun deleteDialog(item: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton("Aceptar") { _, _ ->
            SqlDataBase.tables?.delete(item, "teams")
            updaAdapter()
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
        Log.d("MAIN", "Starting DB")
        SqlDataBase.tables = SQLHelperE(this)
        Log.d("MAIN", "Created DB")


        teams = SqlDataBase.tables!!.retrieveAllTeams()
        val listView = findViewById<ListView>(R.id.lv_player)
        this.adaptador = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, teams
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val buttonAddListView = findViewById<Button>(
            R.id.create_p
        )
        buttonAddListView.setOnClickListener {
            val newTeamId = SqlDataBase.tables!!.getMaxId("teams") + 1
            openForm(newTeamId)
        }
        registerForContextMenu(listView)
    }

    private fun openForm(teamId: Int) {
        val explicitIntent = Intent(this, TeamForm::class.java)
        explicitIntent.putExtra("team_id", teamId)
        callBackIntent.launch(explicitIntent)
    }
}