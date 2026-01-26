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
    private lateinit var adapter: ClienteAdapter
    private var listaCompleta = listOf<Cliente>()
    private var filtroActual = "" // ← NUEVO: Guardar el filtro actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar toolbar
        setSupportActionBar(binding.toolbar)

        // Configurar RecyclerView
        binding.recyclerItems.layoutManager = LinearLayoutManager(this)

        // Inicializar adaptador vacío
        adapter = ClienteAdapter(
            emptyList(),
            onEliminarClick = { cliente ->
                mostrarDialogoEliminar(cliente)
            },
            onEditarClick = { cliente ->
                abrirEditarCliente(cliente)
            }
        )
        binding.recyclerItems.adapter = adapter

        // Observar cambios en la lista de clientes
        viewModel.clientes.observe(this, Observer<List<Cliente>> { clientes ->
            listaCompleta = clientes
            // Reaplicar el filtro actual (si existe)
            filtrarClientes(filtroActual)
        })

        // Configurar SearchView
        configurarBusqueda()

        // Configurar FAB para abrir pantalla de agregar cliente
        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AgregarClienteActivity::class.java)
            startActivity(intent)
        }

        // Cargar clientes desde la API
        viewModel.cargarClientes()
    }

    private fun configurarBusqueda() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarClientes(newText ?: "")
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

    private fun filtrarClientes(query: String) {
        // Limpiar espacios en blanco antes y después
        val queryLimpio = query.trim()
        filtroActual = queryLimpio // ← Guardar el filtro limpio

        val listaFiltrada = if (queryLimpio.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter { cliente ->
                cliente.nombre.contains(queryLimpio, ignoreCase = true) ||
                cliente.apellido.contains(queryLimpio, ignoreCase = true) ||
                cliente.email.contains(queryLimpio, ignoreCase = true) ||
                cliente.telefono.contains(queryLimpio, ignoreCase = true) ||
                cliente.direccion.contains(queryLimpio, ignoreCase = true)
            }
        }
        adapter.actualizarLista(listaFiltrada)
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista de clientes cada vez que regresa a esta pantalla
        viewModel.cargarClientes()
    }

    private fun abrirEditarCliente(cliente: Cliente) {
        val intent = Intent(this, AgregarClienteActivity::class.java).apply {
            putExtra("CLIENTE_ID", cliente.id)
            putExtra("CLIENTE_NOMBRE", cliente.nombre)
            putExtra("CLIENTE_APELLIDO", cliente.apellido)
            putExtra("CLIENTE_EMAIL", cliente.email)
            putExtra("CLIENTE_TELEFONO", cliente.telefono)
            putExtra("CLIENTE_DIRECCION", cliente.direccion)
        }
        startActivity(intent)
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
