package com.mygdx.primelogistics.android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.models.Operation

class PropAdapter(
    private var allOperations: List<Operation>,
    private val onCardClick: (Operation) -> Unit
) : RecyclerView.Adapter<PropAdapter.ViewHolder>() {

    private var displayList: List<Operation> = allOperations.filter { it.statusName == "Pendiente" }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val propReference: TextView = view.findViewById(R.id.prop_ref)
        val propOrigin: TextView = view.findViewById(R.id.txt_origin)
        val propDestination: TextView = view.findViewById(R.id.txt_destination)
        val propCost: TextView = view.findViewById(R.id.prop_cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_operation, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prop = displayList[position]

        holder.propReference.text = prop.orderReference
        holder.propOrigin.text = prop.originPortName
        holder.propDestination.text = prop.destinationPortName
        holder.propCost.text = prop.totalCost.toString()
        holder.itemView.setOnClickListener { onCardClick(prop) }
    }

    override fun getItemCount(): Int = displayList.size

    fun updateData(newList: List<Operation>) {
        this.allOperations = newList
        this.displayList = newList.filter { it.statusName == "Pendiente" }
        notifyDataSetChanged()
    }
}
