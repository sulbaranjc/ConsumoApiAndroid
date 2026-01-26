package com.sulbaranjc.consumoapiandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sulbaranjc.consumoapiandroid.databinding.ActivityMainBinding
import com.sulbaranjc.consumoapiandroid.model.Cliente
import com.sulbaranjc.consumoapiandroid.network.RetrofitClient
import com.sulbaranjc.consumoapiandroid.ui.ClienteAdapter
import com.sulbaranjc.consumoapiandroid.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar toolbar
        setSupportActionBar(binding.toolbar)

        // Configurar RecyclerView
        binding.recyclerItems.layoutManager = LinearLayoutManager(this)

        // Observar cambios en la lista de clientes
        viewModel.clientes.observe(this, Observer<List<Cliente>> { clientes ->
            binding.recyclerItems.adapter = ClienteAdapter(clientes) { cliente ->
                mostrarDialogoEliminar(cliente)
            }
        })

        // Configurar FAB para abrir pantalla de agregar cliente
        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AgregarClienteActivity::class.java)
            startActivity(intent)
        }

        // Cargar clientes desde la API
        viewModel.cargarClientes()
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista de clientes cada vez que regresa a esta pantalla
        viewModel.cargarClientes()
    }

    private fun mostrarDialogoEliminar(cliente: Cliente) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Cliente")
            .setMessage("¿Está seguro que desea eliminar a ${cliente.nombre} ${cliente.apellido}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarCliente(cliente)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarCliente(cliente: Cliente) {
        RetrofitClient.api.eliminarCliente(cliente.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Cliente eliminado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Recargar la lista
                    viewModel.cargarClientes()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al eliminar cliente: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
