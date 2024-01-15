package com.example.examen

import java.util.*

data class DTeam(
    var id: Int,
    var name: String,
    var foundationDate: Date,
    var netIncome: Float,
    var isActive: Boolean,
){
    override fun toString(): String {
        return name;
    }
}
