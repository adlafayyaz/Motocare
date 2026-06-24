package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder

class DashboardActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        dbHelper = MotoCareDbHelper(this)

        findViewById<Button>(R.id.buttonDashboardAddMotor).setOnClickListener {
            startActivity(Intent(this, com.example.motocare.motor.MotorFormActivity::class.java))
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_HOME)
    }

    override fun onResume() {
        super.onResume()
        bindDashboard()
    }

    private fun bindDashboard() {
        val activeMotor = dbHelper.getActiveMotor()
        val hasMotor = activeMotor != null

        findViewById<TextView>(R.id.textDashboardMotorName).text = activeMotor?.name
            ?: getString(R.string.no_active_motor)
        findViewById<TextView>(R.id.textDashboardMotorPlate).text = activeMotor?.plateNumber
            ?: getString(R.string.add_motor_first)
        findViewById<TextView>(R.id.textDashboardKilometer).text = activeMotor?.let {
            getString(R.string.motor_kilometer_value, it.currentKilometer)
        } ?: "-"

        findViewById<TextView>(R.id.textMonthlyTotal).text = getString(
            R.string.rupiah_value,
            dbHelper.getMonthlyExpenseTotal()
        )
        findViewById<TextView>(R.id.textFuelTotal).text = getString(
            R.string.rupiah_value,
            dbHelper.getFuelMonthlyTotal()
        )
        findViewById<TextView>(R.id.textTaxTotal).text = getString(
            R.string.rupiah_value,
            dbHelper.getTaxMonthlyTotal()
        )
        findViewById<TextView>(R.id.textOilTotal).text = getString(
            R.string.rupiah_value,
            dbHelper.getOilMonthlyTotal()
        )
        findViewById<TextView>(R.id.textNextService).text = if (hasMotor) {
            getString(R.string.no_service_data)
        } else {
            getString(R.string.no_motor_estimate)
        }
        findViewById<Button>(R.id.buttonDashboardAddMotor).visibility =
            if (hasMotor) View.GONE else View.VISIBLE
    }
}
