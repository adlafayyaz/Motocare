package com.example.motocare.motor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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

        findViewById<Button>(R.id.buttonEditMotor).setOnClickListener {
            val intent = Intent(this, MotorFormActivity::class.java)
            intent.putExtra(MotorFormActivity.EXTRA_MOTOR_ID, motorId)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        findViewById<Button>(R.id.buttonSetActiveMotor).setOnClickListener {
            dbHelper.setActiveMotor(motorId)
            bindMotor()
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
        findViewById<Button>(R.id.buttonSetActiveMotor).visibility =
            if (current.isActive) View.GONE else View.VISIBLE
    }

    private fun showDeleteConfirm() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_motor_title)
            .setMessage(R.string.delete_motor_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteMotor(motorId)
                finish()
            }
            .show()
    }

    companion object {
        const val EXTRA_MOTOR_ID = "extra_motor_id"
    }
}
