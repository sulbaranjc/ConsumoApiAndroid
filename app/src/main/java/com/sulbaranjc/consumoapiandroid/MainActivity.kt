package com.sulbaranjc.consumoapiandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sulbaranjc.consumoapiandroid.databinding.ActivityMainBinding
import com.sulbaranjc.consumoapiandroid.model.Cliente
import com.sulbaranjc.consumoapiandroid.network.RetrofitClient
import com.sulbaranjc.consumoapiandroid.ui.ClienteAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerItems.layoutManager = LinearLayoutManager(this)

        cargarClientes()
    }

    private fun cargarClientes() {
        RetrofitClient.api.getClientes()
            .enqueue(object : Callback<List<Cliente>> {

                override fun onResponse(
                    call: Call<List<Cliente>>,
                    response: Response<List<Cliente>>
                ) {
                    if (response.isSuccessful) {
                        val clientes = response.body() ?: emptyList()
                        binding.recyclerItems.adapter =
                            ClienteAdapter(clientes)
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
