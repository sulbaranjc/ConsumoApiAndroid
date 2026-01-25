package com.sulbaranjc.consumoapiandroid.network

import com.sulbaranjc.consumoapiandroid.model.Cliente
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("clientes/")
    fun getClientes(): Call<List<Cliente>>
}

