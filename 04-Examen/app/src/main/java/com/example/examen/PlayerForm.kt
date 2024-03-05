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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlayerForm : AppCompatActivity() {
    private var player = DPlayer("", "", false, "", 0, "")
    val calendar = Calendar.getInstance()
    private var teamId = ""
    private var playerId = ""
    private lateinit var selectedDateTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_form)
        playerId = intent.getStringExtra("player_id").toString()
        teamId = intent.getStringExtra("team_id").toString()
        consultar(playerId)
        selectedDateTextView = findViewById(R.id.f_p_debut_date)
        selectedDateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        if (player.id != "") {
            selectedDateTextView.text = player.debutDate
            findViewById<EditText>(R.id.f_p_name).setText(player.name)
            findViewById<EditText>(R.id.f_p_value).setText(player.value.toString())
            findViewById<CheckBox>(R.id.f_p_lesionado).isChecked = player.isInjured
        }

        val backButton = findViewById<Button>(R.id.btn_cancel)
        backButton.setOnClickListener {
            exit("Sin cambios")
        }


        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            crear()
            if (player != null) {
                exit("Jugardor Actualizado")
            } else {
                exit("Jugador Creado", playerId)
            }
            consultar(teamId)
        }
    }

    private fun exit(message: String, newPlayer: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("action", message)
        returnIntent.putExtra("newPlayerId", newPlayer)
        println(message)
        println(newPlayer)
        setResult(
            RESULT_OK, returnIntent
        )
        finish()
    }

    private fun exit(message: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("action", message)
        setResult(
            RESULT_OK, returnIntent
        )
        finish()
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateSelectedDate()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateSelectedDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDate = dateFormat.format(calendar.time)
        selectedDateTextView.text = selectedDate
    }


    fun consultar(id: String): Task<DocumentSnapshot> {
        val db = com.google.firebase.Firebase.firestore
        val citiesRefUnico = db.collection("players")
        return citiesRefUnico.document(id).get().addOnSuccessListener { it ->
            if (it.data != null) {
                player = DPlayer(
                    it.id,
                    it.data!!["debutDate"] as String,
                    it.data!!["isInjured"] as Boolean,
                    it.data?.get("name") as String,
                    it.data!!["value"] as Long,
                    it.data!!["team"] as String,
                )
                var id = it.id
                findViewById<EditText>(R.id.f_p_name).setText(player.name)
                findViewById<EditText>(R.id.f_p_debut_date).setText(player.debutDate)
                findViewById<EditText>(R.id.f_p_value).setText(player.value.toString())
                findViewById<CheckBox>(R.id.f_p_lesionado).isChecked = player.isInjured
            }

        }.addOnFailureListener {

        }
    }

    fun crear() {
        val db = com.google.firebase.ktx.Firebase.firestore
        val coll = db.collection("players")
        val teamData = hashMapOf(
            "name" to this.findViewById<TextView>(R.id.f_p_name).text.toString(),
            "debutDate" to this.findViewById<TextView>(R.id.f_p_debut_date).text.toString(),
            "value" to this.findViewById<TextView>(R.id.f_p_value).text.toString().toLong(),
            "isInjured" to this.findViewById<CheckBox>(R.id.f_p_lesionado).isChecked,
            "team" to teamId,
        )
        if (player.id != "") {
            coll
                .document(player.id)
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

