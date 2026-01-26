package com.sulbaranjc.consumoapiandroid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sulbaranjc.consumoapiandroid.databinding.ItemClienteBinding
import com.sulbaranjc.consumoapiandroid.model.Cliente

class ClienteAdapter(
    private val clientes: List<Cliente>
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

        // El botón eliminar está visible pero sin funcionalidad por ahora
        holder.binding.btnEliminar.setOnClickListener {
            // Sin funcionalidad en esta instancia
        }
    }

    override fun getItemCount(): Int = clientes.size
}

