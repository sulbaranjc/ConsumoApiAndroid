package com.sulbaranjc.consumoapiandroid.network

import com.sulbaranjc.consumoapiandroid.model.Cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("clientes/")
    fun getClientes(): Call<List<Cliente>>

    @POST("clientes/")
    fun crearCliente(@Body cliente: ClienteRequest): Call<Cliente>
}

// Data class para enviar datos sin el ID (el servidor lo genera)
data class ClienteRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val direccion: String
)

