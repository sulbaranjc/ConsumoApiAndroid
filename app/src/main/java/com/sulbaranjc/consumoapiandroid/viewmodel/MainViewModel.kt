package com.sulbaranjc.consumoapiandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sulbaranjc.consumoapiandroid.model.Cliente
import com.sulbaranjc.consumoapiandroid.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> = _clientes

    fun cargarClientes() {
        RetrofitClient.api.getClientes()
            .enqueue(object : Callback<List<Cliente>> {

                override fun onResponse(
                    call: Call<List<Cliente>>,
                    response: Response<List<Cliente>>
                ) {
                    if (response.isSuccessful) {
                        _clientes.value = response.body() ?: emptyList()
                    } else {
                        Log.e("API", "Error HTTP: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                    Log.e("API", "Error de red", t)
                }
            })
    }
}
