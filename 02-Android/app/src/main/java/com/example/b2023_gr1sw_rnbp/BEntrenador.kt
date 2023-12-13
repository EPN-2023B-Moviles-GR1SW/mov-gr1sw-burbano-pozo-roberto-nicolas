package com.example.b2023_gr1sw_rnbp

class BEntrenador (
    var id: Int,
    var nombre: String?,
    var descripcion: String?
){
    override fun toString(): String {
        return "${nombre} - ${descripcion}"
    }
}