package com.sulbaranjc.consumoapiandroid

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sulbaranjc.consumoapiandroid.databinding.ActivityAgregarClienteBinding
import com.sulbaranjc.consumoapiandroid.model.Cliente
import com.sulbaranjc.consumoapiandroid.network.ClienteRequest
import com.sulbaranjc.consumoapiandroid.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // Configurar botón guardar con lógica completa
        binding.btnGuardar.setOnClickListener {
            guardarCliente()
        }
    }

    private fun guardarCliente() {
        // Obtener valores de los campos
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty()) {
            binding.etNombre.error = "El nombre es requerido"
            binding.etNombre.requestFocus()
            return
        }

        if (apellido.isEmpty()) {
            binding.etApellido.error = "El apellido es requerido"
            binding.etApellido.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "El email es requerido"
            binding.etEmail.requestFocus()
            return
        }

        if (telefono.isEmpty()) {
            binding.etTelefono.error = "El teléfono es requerido"
            binding.etTelefono.requestFocus()
            return
        }

        if (direccion.isEmpty()) {
            binding.etDireccion.error = "La dirección es requerida"
            binding.etDireccion.requestFocus()
            return
        }

        // Crear objeto ClienteRequest (sin ID)
        val clienteRequest = ClienteRequest(
            nombre = nombre,
            apellido = apellido,
            email = email,
            telefono = telefono,
            direccion = direccion
        )

        // Deshabilitar botón mientras se guarda
        binding.btnGuardar.isEnabled = false
        binding.btnGuardar.text = "Guardando..."

        // Hacer la petición POST a la API
        RetrofitClient.api.crearCliente(clienteRequest).enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                binding.btnGuardar.isEnabled = true
                binding.btnGuardar.text = "Salvar"

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AgregarClienteActivity,
                        "Cliente guardado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Volver a la pantalla anterior
                    finish()
                } else {
                    Toast.makeText(
                        this@AgregarClienteActivity,
                        "Error al guardar cliente: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                binding.btnGuardar.isEnabled = true
                binding.btnGuardar.text = "Salvar"

                Toast.makeText(
                    this@AgregarClienteActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}

