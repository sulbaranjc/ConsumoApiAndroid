package com.sulbaranjc.consumoapiandroid.model

data class Cliente(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String?,
    val direccion: String?
)

