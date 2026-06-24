package com.example.motocare.servis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.Servis

class ServisAdapter(
    private val onClick: (Servis) -> Unit
) : RecyclerView.Adapter<ServisAdapter.ServisViewHolder>() {
    private val items = mutableListOf<Servis>()

    fun submitList(newItems: List<Servis>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servis, parent, false)
        return ServisViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServisViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class ServisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textServisTitle)
        private val meta: TextView = itemView.findViewById(R.id.textServisMeta)
        private val cost: TextView = itemView.findViewById(R.id.textServisCost)
        private val date: TextView = itemView.findViewById(R.id.textServisDate)

        fun bind(servis: Servis, onClick: (Servis) -> Unit) {
            title.text = servis.serviceType
            meta.text = itemView.context.getString(R.string.servis_item_meta, servis.kilometer)
            cost.text = itemView.context.getString(R.string.rupiah_value_compact, servis.cost)
            date.text = servis.serviceDate
            itemView.setOnClickListener { onClick(servis) }
        }
    }
}
