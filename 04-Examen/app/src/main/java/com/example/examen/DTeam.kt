package com.example.examen

import java.util.*

data class DTeam(
    var id: String,
    var name: String,
    var foundationDate: String,
    var netIncome: Long,
    var isActive: Boolean,
){
    override fun toString(): String {
        return name;
    }
}
