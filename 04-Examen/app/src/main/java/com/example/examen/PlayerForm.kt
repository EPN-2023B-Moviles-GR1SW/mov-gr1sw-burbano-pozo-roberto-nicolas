package com.example.examen

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlayerForm : AppCompatActivity() {
    private val players = MemoryDataBase.players
    private var playerId = -1
    private var teamId = -1
    val calendar = Calendar.getInstance()
    private lateinit var selectedDateTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_form)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        playerId = intent.getIntExtra("player_id", -1)
        teamId = intent.getIntExtra("team_id", -1)
        println(playerId)
        println(teamId)
        val player = players.find { it.id == playerId }

        selectedDateTextView = findViewById(R.id.f_p_debut_date)
        selectedDateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        if (player != null) {
            val selectedDate = dateFormat.format(player.debutDate)
            selectedDateTextView.text = selectedDate
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
            if (player != null) {
                player.name = this.findViewById<TextView>(R.id.f_p_name).text.toString()
                player.value =
                    this.findViewById<TextView>(R.id.f_p_value).text.toString().toFloat()
                player.isInjured = this.findViewById<CheckBox>(R.id.f_p_lesionado).isChecked
                player.debutDate =
                    dateFormat.parse(this.findViewById<TextView>(R.id.f_p_debut_date).text.toString())!!
                exit("Jugardor Actualizado")
            } else {
                players.add(
                    DPlayer(
                        playerId,
                        dateFormat.parse(this.findViewById<TextView>(R.id.f_p_debut_date).text.toString())!!,
                        this.findViewById<CheckBox>(R.id.f_p_lesionado).isChecked,
                        this.findViewById<TextView>(R.id.f_p_name).text.toString(),
                        this.findViewById<TextView>(R.id.f_p_value).text.toString().toFloat(),
                        teamId
                    )
                )
                println(players)
                exit("Jugador Creado",playerId)
            }
        }
    }

    private fun exit(message: String,newPlayer: Int) {
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
}

