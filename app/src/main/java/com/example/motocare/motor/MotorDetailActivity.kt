package com.example.motocare.motor

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Motor
import com.example.motocare.navigation.BottomNavBinder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.ceil

class MotorDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private var motorId: Long = 0
    private var motor: Motor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motor_detail)

        dbHelper = MotoCareDbHelper(this)
        motorId = intent.getLongExtra(EXTRA_MOTOR_ID, 0)

        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        findViewById<Button>(R.id.buttonEditMotor).setOnClickListener {
            val intent = Intent(this, MotorFormActivity::class.java)
            intent.putExtra(MotorFormActivity.EXTRA_MOTOR_ID, motorId)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        findViewById<Button>(R.id.buttonSetActiveMotor).setOnClickListener {
            dbHelper.setActiveMotor(motorId)
            bindMotor()
            Toast.makeText(this, R.string.motor_active, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.buttonDeleteMotor).setOnClickListener {
            showDeleteConfirm()
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_MOTOR)
    }

    override fun onResume() {
        super.onResume()
        bindMotor()
    }

    private fun bindMotor() {
        motor = dbHelper.getMotor(motorId)
        val current = motor ?: run {
            finish()
            return
        }

        findViewById<TextView>(R.id.textDetailMotorName).text = current.name
        findViewById<TextView>(R.id.textDetailPlateNumber).text = current.plateNumber
        val kilometer = NumberFormat.getNumberInstance(Locale("id", "ID")).format(current.currentKilometer)
        findViewById<TextView>(R.id.textDetailKilometer).text = getString(R.string.km_value_short, kilometer)
        findViewById<TextView>(R.id.textDetailActive).text = if (current.isActive) {
            getString(R.string.motor_active)
        } else {
            getString(R.string.motor_inactive)
        }
        bindSummary(current)
        findViewById<Button>(R.id.buttonSetActiveMotor).visibility =
            if (current.isActive) View.GONE else View.VISIBLE
    }

    private fun bindSummary(current: Motor) {
        val serviceDue = dbHelper.getLatestServis(current.id)?.let { servis ->
            val targetKm = serviceTargetKm(servis.kilometer, servis.intervalKm)
            current.currentKilometer >= targetKm
        } == true
        val serviceValue = dbHelper.getLatestServis(current.id)?.let { servis ->
            val targetKm = serviceTargetKm(servis.kilometer, servis.intervalKm)
            if (serviceDue) {
                getString(R.string.service_due_now)
            } else {
                getString(R.string.service_remaining_meta, (targetKm - current.currentKilometer).coerceAtLeast(0))
            }
        } ?: getString(R.string.no_data_short)

        val oilDue = dbHelper.getLatestOli(current.id)?.let { oli ->
            current.currentKilometer >= oli.nextKilometer
        } == true
        val oilValue = dbHelper.getLatestOli(current.id)?.let { oli ->
            if (oilDue) {
                getString(R.string.oil_due_now)
            } else {
                getString(R.string.km_remaining_value, (oli.nextKilometer - current.currentKilometer).coerceAtLeast(0))
            }
        } ?: getString(R.string.no_data_short)

        val pajak = dbHelper.getPajakByMotor(current.id).firstOrNull()
        val taxDue = pajak?.let {
            it.status.equals("Belum bayar", true) && (daysUntil(it.dueDate) ?: 1) <= 0
        } == true
        val taxValue = pajak?.let {
            if (taxDue) getString(R.string.tax_due_now) else it.dueDate
        }
            ?: getString(R.string.no_data_short)

        val fuelValue = dbHelper.getBensinByMotor(current.id).firstOrNull()?.let { bensin ->
            getString(R.string.fuel_liter_value, bensin.liter)
        } ?: getString(R.string.no_data_short)

        bindSummaryText(R.id.textSummaryServiceValue, serviceValue, serviceDue)
        bindSummaryText(R.id.textSummaryOilValue, oilValue, oilDue)
        bindSummaryText(R.id.textSummaryTaxValue, taxValue, taxDue)
        findViewById<TextView>(R.id.textSummaryFuelValue).text = fuelValue
    }

    private fun bindSummaryText(id: Int, value: String, overdue: Boolean) {
        findViewById<TextView>(id).apply {
            text = value
            setTextColor(getColor(if (overdue) R.color.motocare_error else R.color.motocare_muted))
        }
    }

    private fun daysUntil(date: String): Int? {
        return runCatching {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val due = format.parse(date) ?: return null
            val today = format.parse(format.format(System.currentTimeMillis())) ?: return null
            ceil((due.time - today.time) / DAY_MILLIS.toDouble()).toInt()
        }.getOrNull()
    }

    private fun serviceTargetKm(kilometer: Int, targetOrInterval: Int): Int {
        return if (targetOrInterval > kilometer) targetOrInterval else kilometer + targetOrInterval
    }

    private fun showDeleteConfirm() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_delete_motor)
        dialog.findViewById<Button>(R.id.buttonConfirmDelete).setOnClickListener {
            dbHelper.deleteMotor(motorId)
            dialog.dismiss()
            finish()
        }
        dialog.findViewById<Button>(R.id.buttonCancelDelete).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }

    companion object {
        const val EXTRA_MOTOR_ID = "extra_motor_id"
        private const val DAY_MILLIS = 86_400_000L
    }
}
