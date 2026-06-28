package com.example.motocare.motor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.Motor
import java.text.NumberFormat
import java.util.Locale

class MotorAdapter(
    private val onClick: (Motor) -> Unit
) : RecyclerView.Adapter<MotorAdapter.MotorViewHolder>() {
    private val motors = mutableListOf<Motor>()

    fun submitList(newMotors: List<Motor>) {
        motors.clear()
        motors.addAll(newMotors)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_motor, parent, false)
        return MotorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MotorViewHolder, position: Int) {
        holder.bind(motors[position], onClick)
    }

    override fun getItemCount(): Int = motors.size

    class MotorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.textMotorName)
        private val plateText: TextView = itemView.findViewById(R.id.textMotorPlate)
        private val kilometerText: TextView = itemView.findViewById(R.id.textMotorKilometer)
        private val activeText: TextView = itemView.findViewById(R.id.textMotorActive)

        fun bind(motor: Motor, onClick: (Motor) -> Unit) {
            nameText.text = motor.name
            plateText.text = motor.plateNumber
            val kilometer = NumberFormat.getNumberInstance(Locale("id", "ID"))
                .format(motor.currentKilometer)
            kilometerText.text = itemView.context.getString(R.string.km_value_short, kilometer)
            activeText.visibility = if (motor.isActive) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onClick(motor) }
        }
    }
}
