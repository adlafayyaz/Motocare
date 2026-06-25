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
import java.util.Locale

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
        val serviceValue = dbHelper.getLatestServis(current.id)?.let { servis ->
            val targetKm = serviceTargetKm(servis.kilometer, servis.intervalKm)
            getString(R.string.service_remaining_meta, (targetKm - servis.kilometer).coerceAtLeast(0))
        } ?: getString(R.string.no_data_short)

        val oilValue = dbHelper.getLatestOli(current.id)?.let { oli ->
            getString(R.string.km_remaining_value, (oli.nextKilometer - current.currentKilometer).coerceAtLeast(0))
        } ?: getString(R.string.no_data_short)

        val taxValue = dbHelper.getPajakByMotor(current.id).firstOrNull()?.dueDate
            ?: getString(R.string.no_data_short)

        val fuelValue = dbHelper.getBensinByMotor(current.id).firstOrNull()?.let { bensin ->
            getString(R.string.fuel_liter_value, bensin.liter)
        } ?: getString(R.string.no_data_short)

        findViewById<TextView>(R.id.textSummaryServiceValue).text = serviceValue
        findViewById<TextView>(R.id.textSummaryOilValue).text = oilValue
        findViewById<TextView>(R.id.textSummaryTaxValue).text = taxValue
        findViewById<TextView>(R.id.textSummaryFuelValue).text = fuelValue
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
    }
}
