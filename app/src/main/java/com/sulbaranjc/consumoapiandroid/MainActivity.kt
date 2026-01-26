package com.sulbaranjc.consumoapiandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sulbaranjc.consumoapiandroid.databinding.ActivityMainBinding
import com.sulbaranjc.consumoapiandroid.model.Cliente
import com.sulbaranjc.consumoapiandroid.ui.ClienteAdapter
import com.sulbaranjc.consumoapiandroid.viewmodel.MainViewModel

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
            binding.recyclerItems.adapter = ClienteAdapter(clientes)
        })

        // Configurar FAB para abrir pantalla de agregar cliente
        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AgregarClienteActivity::class.java)
            startActivity(intent)
        }

        // Cargar clientes desde la API
        viewModel.cargarClientes()
    }
}
