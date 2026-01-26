package com.sulbaranjc.consumoapiandroid.utils

import android.util.Patterns
import java.util.regex.Pattern

/**
 * Validador de campos de Cliente según contrato técnico del backend
 * Implementa las mismas reglas que el backend para consistencia
 */
object ClienteValidator {

    // Regex para nombres y apellidos (letras y espacios, incluyendo caracteres españoles)
    private val NOMBRE_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")

    // Regex para teléfono (formato backend: +?dígitos, 7-15 dígitos)
    private val TELEFONO_PATTERN = Pattern.compile("^\\+?\\d{7,15}$")

    /**
     * Resultado de validación
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String = ""
    )

    /**
     * Valida el campo nombre según contrato técnico
     * - Obligatorio
     * - 2-50 caracteres
     * - Solo letras y espacios
     * - Caracteres españoles permitidos
     */
    fun validateNombre(nombre: String): ValidationResult {
        val trimmedNombre = nombre.trim()

        return when {
            trimmedNombre.isEmpty() ->
                ValidationResult(false, "El nombre es obligatorio")

            trimmedNombre.length < 2 ->
                ValidationResult(false, "El nombre debe tener al menos 2 caracteres")

            trimmedNombre.length > 50 ->
                ValidationResult(false, "El nombre no puede exceder 50 caracteres")

            trimmedNombre.isBlank() ->
                ValidationResult(false, "El nombre no puede contener solo espacios")

            !NOMBRE_PATTERN.matcher(trimmedNombre).matches() ->
                ValidationResult(false, "El nombre solo puede contener letras y espacios")

            else -> ValidationResult(true)
        }
    }

    /**
     * Valida el campo apellido según contrato técnico
     * - Obligatorio
     * - 2-50 caracteres
     * - Solo letras y espacios
     * - Caracteres españoles permitidos
     */
    fun validateApellido(apellido: String): ValidationResult {
        val trimmedApellido = apellido.trim()

        return when {
            trimmedApellido.isEmpty() ->
                ValidationResult(false, "El apellido es obligatorio")

            trimmedApellido.length < 2 ->
                ValidationResult(false, "El apellido debe tener al menos 2 caracteres")

            trimmedApellido.length > 50 ->
                ValidationResult(false, "El apellido no puede exceder 50 caracteres")

            trimmedApellido.isBlank() ->
                ValidationResult(false, "El apellido no puede contener solo espacios")

            !NOMBRE_PATTERN.matcher(trimmedApellido).matches() ->
                ValidationResult(false, "El apellido solo puede contener letras y espacios")

            else -> ValidationResult(true)
        }
    }

    /**
     * Valida el campo email según contrato técnico
     * - Obligatorio
     * - Formato email válido
     */
    fun validateEmail(email: String): ValidationResult {
        val trimmedEmail = email.trim()

        return when {
            trimmedEmail.isEmpty() ->
                ValidationResult(false, "El email es obligatorio")

            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() ->
                ValidationResult(false, "Formato de email inválido")

            else -> ValidationResult(true)
        }
    }

    /**
     * Valida el campo teléfono según contrato técnico
     * - Opcional (puede ser null o vacío)
     * - Si se proporciona: 7-15 dígitos, puede comenzar con +
     * - Se eliminan espacios, guiones y paréntesis antes de validar
     */
    fun validateTelefono(telefono: String?): ValidationResult {
        if (telefono.isNullOrBlank()) {
            return ValidationResult(true) // Es opcional
        }

        // Limpiar teléfono: eliminar espacios, guiones y paréntesis
        val telefonoLimpio = telefono.trim()
            .replace(" ", "")
            .replace("-", "")
            .replace("(", "")
            .replace(")", "")

        return when {
            !TELEFONO_PATTERN.matcher(telefonoLimpio).matches() ->
                ValidationResult(false, "El teléfono debe contener entre 7 y 15 dígitos")

            else -> ValidationResult(true)
        }
    }

    /**
     * Valida el campo dirección según contrato técnico
     * - Opcional (puede ser null o vacío)
     * - Si se proporciona: máximo 200 caracteres
     */
    fun validateDireccion(direccion: String?): ValidationResult {
        if (direccion.isNullOrBlank()) {
            return ValidationResult(true) // Es opcional
        }

        val trimmedDireccion = direccion.trim()

        return when {
            trimmedDireccion.length > 200 ->
                ValidationResult(false, "La dirección no puede exceder 200 caracteres")

            else -> ValidationResult(true)
        }
    }

    /**
     * Valida todos los campos de un cliente según contrato técnico
     * Retorna el primer error encontrado o éxito si todo está correcto
     */
    fun validateCliente(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String?,
        direccion: String?
    ): ValidationResult {
        val nombreResult = validateNombre(nombre)
        if (!nombreResult.isValid) return nombreResult

        val apellidoResult = validateApellido(apellido)
        if (!apellidoResult.isValid) return apellidoResult

        val emailResult = validateEmail(email)
        if (!emailResult.isValid) return emailResult

        val telefonoResult = validateTelefono(telefono)
        if (!telefonoResult.isValid) return telefonoResult

        val direccionResult = validateDireccion(direccion)
        if (!direccionResult.isValid) return direccionResult

        return ValidationResult(true)
    }

    /**
     * Convierte string vacío o solo espacios a null para campos opcionales
     */
    fun stringToNullIfEmpty(value: String?): String? {
        return value?.trim()?.takeIf { it.isNotEmpty() }
    }
}
