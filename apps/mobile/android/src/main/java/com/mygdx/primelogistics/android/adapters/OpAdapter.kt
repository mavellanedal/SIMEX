package com.mygdx.primelogistics.android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.models.Operation

class OpAdapter(
    private val operations: MutableList<Operation>,
    private val onCardClick: (Operation) -> Unit
) : RecyclerView.Adapter<OpAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val opIdentification: TextView = view.findViewById(R.id.op_id)
        val opOrigin: TextView = view.findViewById(R.id.txt_origin)
        val opDestination: TextView = view.findViewById(R.id.txt_destination)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.item_operacion, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val operation = operations[position]
        val context = holder.itemView.context

        holder.opIdentification.text = operation.id.toString()
        holder.opOrigin.text = operation.originPort
        holder.opDestination.text = operation.destinationPort
        holder.itemView.setOnClickListener { onCardClick(operation) }
    }

    override fun getItemCount(): Int = operations.size
}
