package com.example.examen

import java.util.*

data class DPlayer(
    var id: Int,
    var debutDate: Date,
    var isInjured: Boolean,
    var name: String,
    var value: Float,
    var team: Int
){
    override fun toString(): String {
        return "$name $team $id";
    }
}
