package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Pajak
import com.example.motocare.navigation.BottomNavBinder
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.ceil

class DashboardActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        dbHelper = MotoCareDbHelper(this)

        findViewById<Button>(R.id.buttonDashboardAddMotor).setOnClickListener {
            startActivity(Intent(this, com.example.motocare.motor.MotorFormActivity::class.java))
        }
        findViewById<TextView>(R.id.buttonDashboardProfile).setOnClickListener {
            startActivity(Intent(this, com.example.motocare.profile.ProfileActivity::class.java))
            overridePendingTransition(0, 0)
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
        findViewById<TextView>(R.id.textTransactionCount).text = getString(
            R.string.transactions_count_value,
            dbHelper.getRecordCount()
        )
        findViewById<TextView>(R.id.textFuelTotal).text = getString(
            R.string.rupiah_value,
            dbHelper.getFuelMonthlyTotal()
        )

        if (activeMotor == null) {
            bindEmptyEstimate()
        } else {
            bindServiceEstimate(activeMotor.id, activeMotor.currentKilometer)
            bindOilEstimate(activeMotor.id, activeMotor.currentKilometer)
            bindTaxEstimate(activeMotor.id)
        }
        findViewById<Button>(R.id.buttonDashboardAddMotor).visibility =
            if (hasMotor) View.GONE else View.VISIBLE
    }

    private fun bindEmptyEstimate() {
        findViewById<TextView>(R.id.textNextService).text = getString(R.string.next_service_title)
        findViewById<TextView>(R.id.textNextServiceMeta).text = getString(R.string.no_motor_estimate)
        findViewById<TextView>(R.id.textNextServiceValue).text = "-"
        findViewById<TextView>(R.id.textNextServiceLabel).text = getString(R.string.target_label)
        findViewById<TextView>(R.id.textTaxTotal).text = "-"
        findViewById<TextView>(R.id.textOilTotal).text = "-"
    }

    private fun bindServiceEstimate(motorId: Long, currentKilometer: Int) {
        val latest = dbHelper.getLatestServis(motorId)
        findViewById<TextView>(R.id.textNextService).text = getString(R.string.next_service_title)
        if (latest == null) {
            findViewById<TextView>(R.id.textNextServiceMeta).text = getString(R.string.no_service_data_short)
            findViewById<TextView>(R.id.textNextServiceValue).text = "-"
            findViewById<TextView>(R.id.textNextServiceLabel).text = getString(R.string.target_label)
            return
        }

        val targetKm = latest.kilometer + latest.intervalKm
        val remainingKm = (targetKm - currentKilometer).coerceAtLeast(0)
        findViewById<TextView>(R.id.textNextServiceMeta).text = getString(
            R.string.service_remaining_meta,
            remainingKm
        )
        findViewById<TextView>(R.id.textNextServiceValue).text = getString(
            R.string.km_value_short,
            targetKm
        )
        findViewById<TextView>(R.id.textNextServiceLabel).text = getString(R.string.target_label)
    }

    private fun bindOilEstimate(motorId: Long, currentKilometer: Int) {
        val latest = dbHelper.getLatestOli(motorId)
        findViewById<TextView>(R.id.textOilTotal).text = if (latest == null) {
            getString(R.string.no_data_short)
        } else {
            val remainingKm = (latest.nextKilometer - currentKilometer).coerceAtLeast(0)
            getString(R.string.km_remaining_value, remainingKm)
        }
    }

    private fun bindTaxEstimate(motorId: Long) {
        val nearest = dbHelper.getPajakByMotor(motorId).firstOrNull { it.status.equals("Belum bayar", true) }
            ?: dbHelper.getPajakByMotor(motorId).firstOrNull()
        findViewById<TextView>(R.id.textTaxTotal).text = nearest?.let { pajak ->
            val days = daysUntil(pajak)
            if (days == null) getString(R.string.rupiah_value, pajak.cost) else getString(R.string.days_value, days)
        } ?: getString(R.string.no_data_short)
    }

    private fun daysUntil(pajak: Pajak): Int? {
        return runCatching {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val due = format.parse(pajak.dueDate) ?: return null
            val today = format.parse(format.format(System.currentTimeMillis())) ?: return null
            val millis = due.time - today.time
            ceil(millis / DAY_MILLIS.toDouble()).toInt()
        }.getOrNull()
    }

    private companion object {
        const val DAY_MILLIS = 86_400_000L
    }
}
