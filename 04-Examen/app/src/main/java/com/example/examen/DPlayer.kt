package com.example.examen

data class DPlayer(
    var id: String,
    var debutDate: String,
    var isInjured: Boolean,
    var name: String,
    var value: Long,
    var team: String,

    ){
    override fun toString(): String {
        return "$name";
    }
}
