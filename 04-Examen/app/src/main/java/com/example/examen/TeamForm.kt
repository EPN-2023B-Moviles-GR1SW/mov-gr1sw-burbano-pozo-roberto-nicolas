package com.example.examen

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TeamForm : AppCompatActivity() {
    private var teamId: String = ""
    private var team = DTeam("", "", "", 0, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_form)

        teamId = intent.getStringExtra("team_id")!!

        consultar(teamId)


        val backButton = findViewById<Button>(R.id.btn_cancel_t)
        backButton.setOnClickListener {
            exit("Sin cambios")
        }


        val saveButton = findViewById<Button>(R.id.btn_save_t)
        saveButton.setOnClickListener {
            crear()
            if (team.id != "2") {
                exit("Equipo Actualizado")
            } else {
                exit("Equipo Creado")
            }
        }
    }

    private fun exit(message: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("action", message)
        setResult(
            RESULT_OK, returnIntent
        )
        finish()
    }


    fun consultar(id: String): Task<DocumentSnapshot> {
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("teams")
        return citiesRefUnico.document(id).get().addOnSuccessListener { it ->
            if (it.data != null) {
                Log.e("TEAM", it.toString())
                team = DTeam(
                    it.id,
                    it.data?.get("name") as String,
                    it.data!!["foundationDate"] as String,
                    it.data!!["netIncome"] as Long,
                    it.data!!["isActive"] as Boolean
                )
                var id = it.id
                "ID: $id".also { findViewById<TextView>(R.id.id_label_t).text = id }
                findViewById<EditText>(R.id.name_t).setText(team.name)
                findViewById<EditText>(R.id.foundation_t).setText(team.foundationDate)
                findViewById<EditText>(R.id.income_t).setText(team.netIncome.toString())
                findViewById<CheckBox>(R.id.active_t).isChecked = team.isActive
            }

        }.addOnFailureListener {

        }
    }

    fun crear() {
        val db = com.google.firebase.ktx.Firebase.firestore
        val coll = db.collection("teams")
        val teamData = hashMapOf(
            "name" to this.findViewById<TextView>(R.id.name_t).text.toString(),
            "foundationDate" to this.findViewById<TextView>(R.id.foundation_t).text.toString(),
            "netIncome" to this.findViewById<TextView>(R.id.income_t).text.toString().toLong(),
            "isActive" to this.findViewById<CheckBox>(R.id.active_t).isChecked,
        )
        if (team.id != "") {
            coll
                .document(team.id)
                .set(teamData)
                .addOnSuccessListener { }
                .addOnFailureListener { }
        } else {
            coll
                .add(teamData)
                .addOnCompleteListener { }
                .addOnFailureListener { }
        }
    }
}

