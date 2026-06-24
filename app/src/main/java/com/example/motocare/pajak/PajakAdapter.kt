package com.example.motocare.pajak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.Pajak

class PajakAdapter(
    private val onClick: (Pajak) -> Unit
) : RecyclerView.Adapter<PajakAdapter.PajakViewHolder>() {
    private val items = mutableListOf<Pajak>()

    fun submitList(newItems: List<Pajak>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PajakViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pajak, parent, false)
        return PajakViewHolder(view)
    }

    override fun onBindViewHolder(holder: PajakViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class PajakViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textPajakTitle)
        private val meta: TextView = itemView.findViewById(R.id.textPajakMeta)
        private val cost: TextView = itemView.findViewById(R.id.textPajakCost)
        private val status: TextView = itemView.findViewById(R.id.textPajakStatus)

        fun bind(pajak: Pajak, onClick: (Pajak) -> Unit) {
            title.text = itemView.context.getString(R.string.tax_due_value, pajak.dueDate)
            meta.text = pajak.status
            cost.text = itemView.context.getString(R.string.rupiah_value_compact, pajak.cost)
            status.text = pajak.status
            itemView.setOnClickListener { onClick(pajak) }
        }
    }
}
