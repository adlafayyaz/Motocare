package com.example.motocare.oli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.Oli

class OliAdapter(
    private val onClick: (Oli) -> Unit
) : RecyclerView.Adapter<OliAdapter.OliViewHolder>() {
    private val items = mutableListOf<Oli>()

    fun submitList(newItems: List<Oli>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OliViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_oli, parent, false)
        return OliViewHolder(view)
    }

    override fun onBindViewHolder(holder: OliViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class OliViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textOliTitle)
        private val meta: TextView = itemView.findViewById(R.id.textOliMeta)
        private val remain: TextView = itemView.findViewById(R.id.textOliRemain)
        private val date: TextView = itemView.findViewById(R.id.textOliDate)

        fun bind(oli: Oli, onClick: (Oli) -> Unit) {
            title.text = oli.oilType
            meta.text = itemView.context.getString(R.string.oli_item_meta, oli.kilometer)
            remain.text = itemView.context.getString(R.string.oli_next_km_value, oli.nextKilometer)
            date.text = oli.oilChangeDate
            itemView.setOnClickListener { onClick(oli) }
        }
    }
}
