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
    private var clienteId: Int = -1
    private var isEditMode: Boolean = false

    // Variables para almacenar los datos originales del cliente
    private var nombreOriginal: String = ""
    private var apellidoOriginal: String = ""
    private var emailOriginal: String = ""
    private var telefonoOriginal: String = ""
    private var direccionOriginal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAgregarClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Detectar si estamos en modo edición
        clienteId = intent.getIntExtra("CLIENTE_ID", -1)
        isEditMode = clienteId != -1

        if (isEditMode) {
            configurarModoEditar()
        } else {
            configurarModoCrear()
        }

        // Configurar botón volver
        binding.btnVolver.setOnClickListener {
            finish()
        }

        // Configurar botón guardar con lógica completa
        binding.btnGuardar.setOnClickListener {
            guardarCliente()
        }
    }

    private fun configurarModoEditar() {
        // Cambiar título y texto del botón
        binding.tvTitulo.text = "Editar"
        binding.btnGuardar.text = "Actualizar"

        // Obtener datos del intent y almacenar como originales
        nombreOriginal = intent.getStringExtra("CLIENTE_NOMBRE") ?: ""
        apellidoOriginal = intent.getStringExtra("CLIENTE_APELLIDO") ?: ""
        emailOriginal = intent.getStringExtra("CLIENTE_EMAIL") ?: ""
        telefonoOriginal = intent.getStringExtra("CLIENTE_TELEFONO") ?: ""
        direccionOriginal = intent.getStringExtra("CLIENTE_DIRECCION") ?: ""

        // Prellenar los campos con los datos del cliente
        binding.etNombre.setText(nombreOriginal)
        binding.etApellido.setText(apellidoOriginal)
        binding.etEmail.setText(emailOriginal)
        binding.etTelefono.setText(telefonoOriginal)
        binding.etDireccion.setText(direccionOriginal)
    }

    private fun configurarModoCrear() {
        // Mantener título y botón por defecto
        binding.btnGuardar.text = "Salvar"
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

        // Si estamos en modo edición, verificar si hay cambios
        if (isEditMode && !hayChangios(nombre, apellido, email, telefono, direccion)) {
            // No hay cambios, simular actualización exitosa y regresar
            finish()
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
        binding.btnGuardar.text = if (isEditMode) "Actualizando..." else "Guardando..."

        // Decidir si crear o actualizar
        if (isEditMode) {
            actualizarCliente(clienteRequest)
        } else {
            crearCliente(clienteRequest)
        }
    }

    private fun crearCliente(clienteRequest: ClienteRequest) {
        // Hacer la petición POST a la API
        RetrofitClient.api.crearCliente(clienteRequest).enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                binding.btnGuardar.isEnabled = true
                binding.btnGuardar.text = "Salvar"

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AgregarClienteActivity,
                        "Cliente creado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AgregarClienteActivity,
                        "Error al crear cliente: ${response.code()}",
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

    private fun actualizarCliente(clienteRequest: ClienteRequest) {
        // Hacer la petición PUT a la API
        RetrofitClient.api.actualizarCliente(clienteId, clienteRequest).enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                binding.btnGuardar.isEnabled = true
                binding.btnGuardar.text = "Actualizar"

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AgregarClienteActivity,
                        "Cliente actualizado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AgregarClienteActivity,
                        "Error al actualizar cliente: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                binding.btnGuardar.isEnabled = true
                binding.btnGuardar.text = "Actualizar"

                Toast.makeText(
                    this@AgregarClienteActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun hayChangios(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        direccion: String
    ): Boolean {
        return nombre != nombreOriginal ||
               apellido != apellidoOriginal ||
               email != emailOriginal ||
               telefono != telefonoOriginal ||
               direccion != direccionOriginal
    }
}

