package com.sulbaranjc.consumoapiandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sulbaranjc.consumoapiandroid.databinding.ActivityAgregarClienteBinding

class AgregarClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAgregarClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón volver
        binding.btnVolver.setOnClickListener {
            finish()
        }

        // Configurar botón guardar (sin funcionalidad por ahora)
        binding.btnGuardar.setOnClickListener {
            // Aquí se implementará la lógica para guardar el cliente
            // Por ahora solo muestra que el botón funciona
        }
    }
}

