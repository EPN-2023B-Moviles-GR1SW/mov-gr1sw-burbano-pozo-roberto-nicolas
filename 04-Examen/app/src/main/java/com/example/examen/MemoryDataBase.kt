package com.example.examen

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MemoryDataBase {

    companion object {
        val teams: ArrayList<DTeam>
        val players: ArrayList<DPlayer>

        init {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            teams = arrayListOf(
                DTeam(0, "Barcelona F.C.", dateFormatter.parse("1960-01-02"), 893249.3F, true),
                DTeam(1, "Manchester City", dateFormatter.parse("1965-05-05"), 654564.5F, true),
                DTeam(2, "Espoli", dateFormatter.parse("1989-06-34"), 2149.3F, false),
                DTeam(3, "Real Madrid", dateFormatter.parse("1960-01-02"), 893249.3F, true),
                DTeam(4, "Barcelona F.C.", dateFormatter.parse("1960-01-02"), 893249.3F, true),
            )
            players = arrayListOf(
                DPlayer(
                    id = 10,
                    debutDate = dateFormatter.parse("2023-05-20")!!,
                    isInjured = false,
                    name = "Lionel Swift",
                    value = 75.5f,
                    team = 1
                ),
                DPlayer(
                    id = 22,
                    debutDate = dateFormatter.parse("2022-09-10")!!,
                    isInjured = true,
                    name = "Alessia Hernandez",
                    value = 60.2f,
                    team = 2
                ),
                DPlayer(
                    id = 7,
                    debutDate = dateFormatter.parse("2024-01-01")!!,
                    isInjured = false,
                    name = "Rafael Silva",
                    value = 45.8f,
                    team = 1
                ),
                DPlayer(
                    id = 15,
                    debutDate = dateFormatter.parse("2023-11-15")!!,
                    isInjured = false,
                    name = "Sophie Morgan",
                    value = 55.0f,
                    team = 3
                ),
                DPlayer(
                    id = 9,
                    debutDate = dateFormatter.parse("2022-12-30")!!,
                    isInjured = true,
                    name = "Diego Costa",
                    value = 68.3f,
                    team = 2
                )
            )
        }
    }
}