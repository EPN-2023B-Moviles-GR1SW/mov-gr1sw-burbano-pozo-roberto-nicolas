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

class TeamForm : AppCompatActivity() {
    private val teams = MemoryDataBase.teams
    private var teamId = -1
    val calendar = Calendar.getInstance()
    private lateinit var selectedDateTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_form)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        teamId = intent.getIntExtra("team_id", -1)
        val team = teams.find { it.id == teamId }
        "ID: $teamId".also { findViewById<TextView>(R.id.id_label_t).text = it }

        selectedDateTextView = findViewById(R.id.foundation_t)
        selectedDateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        if (team != null) {
            val selectedDate = dateFormat.format(team.foundationDate)
            selectedDateTextView.text = selectedDate
            findViewById<EditText>(R.id.name_t).setText(team.name)
            findViewById<EditText>(R.id.income_t).setText(team.netIncome.toString())
            findViewById<CheckBox>(R.id.active_t).isChecked = team.isActive
        }

        val backButton = findViewById<Button>(R.id.btn_cancel_t)
        backButton.setOnClickListener {
            exit("Sin cambios")
        }


        val saveButton = findViewById<Button>(R.id.btn_save_t)
        saveButton.setOnClickListener {
            if (team != null) {
                team.name = this.findViewById<TextView>(R.id.name_t).text.toString()
                team.netIncome =
                    this.findViewById<TextView>(R.id.income_t).text.toString().toFloat()
                team.isActive = this.findViewById<CheckBox>(R.id.active_t).isChecked
                team.foundationDate =
                    dateFormat.parse(this.findViewById<TextView>(R.id.foundation_t).text.toString())!!
                exit("Equipo Actualizado")
            } else {
                teams.add(
                    DTeam(
                        teamId, this.findViewById<TextView>(R.id.name_t).text.toString(),
                        dateFormat.parse(this.findViewById<TextView>(R.id.foundation_t).text.toString())!!,
                        this.findViewById<TextView>(R.id.income_t).text.toString().toFloat(),
                        this.findViewById<CheckBox>(R.id.active_t).isChecked,
                    )
                )
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

