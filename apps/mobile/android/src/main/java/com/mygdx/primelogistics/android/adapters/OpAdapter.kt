package com.mygdx.primelogistics.android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.models.Operation

class OpAdapter(
    private var operations: MutableList<Operation>,
    private val onCardClick: (Operation) -> Unit
) : RecyclerView.Adapter<OpAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val opReference: TextView = view.findViewById(R.id.op_ref)
        val opOrigin: TextView = view.findViewById(R.id.txt_origin)
        val opDestination: TextView = view.findViewById(R.id.txt_destination)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_operation, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val op = operations[position]

        holder.opReference.text = op.orderReference
        holder.opOrigin.text = op.originPortName
        holder.opDestination.text = op.destinationPortName
        holder.itemView.setOnClickListener { onCardClick(op) }
    }

    fun updateData(newList: List<Operation>) {
        this.operations.clear()
        this.operations.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = operations.size
}
