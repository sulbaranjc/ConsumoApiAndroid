package com.sulbaranjc.consumoapiandroid

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sulbaranjc.consumoapiandroid.databinding.ActivityAgregarClienteBinding
import com.sulbaranjc.consumoapiandroid.model.Cliente
import com.sulbaranjc.consumoapiandroid.network.ClienteRequest
import com.sulbaranjc.consumoapiandroid.network.RetrofitClient
import com.sulbaranjc.consumoapiandroid.utils.ClienteValidator
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
    private var telefonoOriginal: String? = null  // Opcional
    private var direccionOriginal: String? = null // Opcional

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

        // Convertir strings vacíos a null para comparación correcta
        val telefonoIntent = intent.getStringExtra("CLIENTE_TELEFONO") ?: ""
        val direccionIntent = intent.getStringExtra("CLIENTE_DIRECCION") ?: ""

        telefonoOriginal = if (telefonoIntent.isBlank()) null else telefonoIntent
        direccionOriginal = if (direccionIntent.isBlank()) null else direccionIntent

        // Prellenar los campos con los datos del cliente
        binding.etNombre.setText(nombreOriginal)
        binding.etApellido.setText(apellidoOriginal)
        binding.etEmail.setText(emailOriginal)
        binding.etTelefono.setText(telefonoOriginal ?: "")
        binding.etDireccion.setText(direccionOriginal ?: "")
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
        val telefono = binding.etTelefono.text.toString()
        val direccion = binding.etDireccion.text.toString()

        // Limpiar errores previos
        binding.etNombre.error = null
        binding.etApellido.error = null
        binding.etEmail.error = null
        binding.etTelefono.error = null
        binding.etDireccion.error = null

        // Validar campos obligatorios según contrato técnico
        val nombreResult = ClienteValidator.validateNombre(nombre)
        if (!nombreResult.isValid) {
            binding.etNombre.error = nombreResult.errorMessage
            binding.etNombre.requestFocus()
            return
        }

        val apellidoResult = ClienteValidator.validateApellido(apellido)
        if (!apellidoResult.isValid) {
            binding.etApellido.error = apellidoResult.errorMessage
            binding.etApellido.requestFocus()
            return
        }

        val emailResult = ClienteValidator.validateEmail(email)
        if (!emailResult.isValid) {
            binding.etEmail.error = emailResult.errorMessage
            binding.etEmail.requestFocus()
            return
        }

        // Validar campos opcionales si tienen contenido
        val telefonoResult = ClienteValidator.validateTelefono(telefono)
        if (!telefonoResult.isValid) {
            binding.etTelefono.error = telefonoResult.errorMessage
            binding.etTelefono.requestFocus()
            return
        }

        val direccionResult = ClienteValidator.validateDireccion(direccion)
        if (!direccionResult.isValid) {
            binding.etDireccion.error = direccionResult.errorMessage
            binding.etDireccion.requestFocus()
            return
        }

        // Convertir campos opcionales vacíos a null según contrato
        val telefonoFinal = ClienteValidator.stringToNullIfEmpty(telefono)
        val direccionFinal = ClienteValidator.stringToNullIfEmpty(direccion)

        // Si estamos en modo edición, verificar si hay cambios
        if (isEditMode && !hayChangios(nombre, apellido, email, telefonoFinal, direccionFinal)) {
            // No hay cambios, simular actualización exitosa y regresar
            finish()
            return
        }

        // Crear objeto ClienteRequest según nuevo contrato
        val clienteRequest = ClienteRequest(
            nombre = nombre,
            apellido = apellido,
            email = email,
            telefono = telefonoFinal,
            direccion = direccionFinal
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
                    // Manejar error 422 según contrato técnico
                    val errorMessage = if (response.code() == 422) {
                        "Datos inválidos. Revisa los campos marcados."
                    } else {
                        "Error al crear cliente: ${response.code()}"
                    }

                    Toast.makeText(
                        this@AgregarClienteActivity,
                        errorMessage,
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
                    // Manejar error 422 según contrato técnico
                    val errorMessage = if (response.code() == 422) {
                        "Datos inválidos. Revisa los campos marcados."
                    } else {
                        "Error al actualizar cliente: ${response.code()}"
                    }

                    Toast.makeText(
                        this@AgregarClienteActivity,
                        errorMessage,
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
        telefono: String?,
        direccion: String?
    ): Boolean {
        return nombre != nombreOriginal ||
               apellido != apellidoOriginal ||
               email != emailOriginal ||
               telefono != telefonoOriginal ||
               direccion != direccionOriginal
    }
}

