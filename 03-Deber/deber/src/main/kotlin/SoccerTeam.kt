package org.example

import java.util.*

data class SoccerTeam(
    val id: Int,
    val name: String,
    val foundationDate: Date,
    val netIncome: Float,
    val isActive: Boolean,
    val players: MutableList<Int>
)
