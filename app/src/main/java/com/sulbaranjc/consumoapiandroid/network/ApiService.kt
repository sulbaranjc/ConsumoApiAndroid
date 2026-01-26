package com.sulbaranjc.consumoapiandroid.network

import com.sulbaranjc.consumoapiandroid.model.Cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("clientes/")
    fun getClientes(): Call<List<Cliente>>

    @POST("clientes/")
    fun crearCliente(@Body cliente: ClienteRequest): Call<Cliente>

    @PUT("clientes/{id}/")
    fun actualizarCliente(@Path("id") id: Int, @Body cliente: ClienteRequest): Call<Cliente>

    @DELETE("clientes/{id}/")
    fun eliminarCliente(@Path("id") id: Int): Call<Void>
}

// Data class para enviar datos sin el ID (el servidor lo genera)
data class ClienteRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val direccion: String
)

