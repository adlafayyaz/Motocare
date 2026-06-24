package com.example.motocare.bensin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.Bensin

class BensinAdapter(
    private val onClick: (Bensin) -> Unit
) : RecyclerView.Adapter<BensinAdapter.BensinViewHolder>() {
    private val items = mutableListOf<Bensin>()

    fun submitList(newItems: List<Bensin>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BensinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bensin, parent, false)
        return BensinViewHolder(view)
    }

    override fun onBindViewHolder(holder: BensinViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class BensinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textBensinTitle)
        private val meta: TextView = itemView.findViewById(R.id.textBensinMeta)
        private val cost: TextView = itemView.findViewById(R.id.textBensinCost)
        private val date: TextView = itemView.findViewById(R.id.textBensinDate)

        fun bind(bensin: Bensin, onClick: (Bensin) -> Unit) {
            title.text = itemView.context.getString(R.string.fuel_title_value, bensin.fuelBrand, bensin.octane)
            meta.text = itemView.context.getString(R.string.fuel_item_meta, bensin.liter, bensin.kilometer)
            cost.text = itemView.context.getString(R.string.rupiah_value_compact, bensin.cost)
            date.text = bensin.fuelDate
            itemView.setOnClickListener { onClick(bensin) }
        }
    }
}
