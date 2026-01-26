package com.sulbaranjc.consumoapiandroid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sulbaranjc.consumoapiandroid.databinding.ItemClienteBinding
import com.sulbaranjc.consumoapiandroid.model.Cliente

class ClienteAdapter(
    private var clientes: List<Cliente>,
    private val onEliminarClick: (Cliente) -> Unit,
    private val onEditarClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemClienteBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClienteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cliente = clientes[position]

        holder.binding.txtNombre.text =
            "${cliente.nombre} ${cliente.apellido}"

        holder.binding.txtTelefono.text = cliente.telefono

        // Configurar click en botón eliminar
        holder.binding.btnEliminar.setOnClickListener {
            onEliminarClick(cliente)
        }

        // Configurar click en la tarjeta completa para editar
        holder.binding.root.setOnClickListener {
            onEditarClick(cliente)
        }
    }

    override fun getItemCount(): Int = clientes.size

    // Método para actualizar la lista
    fun actualizarLista(nuevaLista: List<Cliente>) {
        clientes = nuevaLista
        notifyDataSetChanged()
    }
}

