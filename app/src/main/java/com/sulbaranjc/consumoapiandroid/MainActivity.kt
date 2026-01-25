package com.sulbaranjc.consumoapiandroid

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

        binding.recyclerItems.layoutManager = LinearLayoutManager(this)

        viewModel.clientes.observe(this, Observer<List<Cliente>> { clientes ->
            binding.recyclerItems.adapter = ClienteAdapter(clientes)
        })

        viewModel.cargarClientes()
    }
}
