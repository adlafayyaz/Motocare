package com.example.motocare.setup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.DashboardActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Motor

class SetupMotorActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var motorNameInput: EditText
    private lateinit var plateNumberInput: EditText
    private lateinit var kilometerInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_motor)

        dbHelper = MotoCareDbHelper(this)
        motorNameInput = findViewById(R.id.editMotorName)
        plateNumberInput = findViewById(R.id.editPlateNumber)
        kilometerInput = findViewById(R.id.editCurrentKilometer)

        findViewById<Button>(R.id.buttonSaveMotor).setOnClickListener {
            if (isValid()) {
                dbHelper.insertMotor(
                    Motor(
                        name = motorNameInput.text.toString().trim(),
                        plateNumber = plateNumberInput.text.toString().trim(),
                        currentKilometer = kilometerInput.text.toString().trim().toInt(),
                        isActive = true
                    )
                )
                Toast.makeText(this, R.string.setup_motor_saved, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }
    }

    private fun isValid(): Boolean {
        var valid = true
        if (motorNameInput.text.isBlank()) {
            motorNameInput.error = getString(R.string.error_motor_name_required)
            valid = false
        }
        if (plateNumberInput.text.isBlank()) {
            plateNumberInput.error = getString(R.string.error_plate_number_required)
            valid = false
        }
        if (kilometerInput.text.isBlank()) {
            kilometerInput.error = getString(R.string.error_kilometer_required)
            valid = false
        } else if (kilometerInput.text.toString().toIntOrNull() == null) {
            kilometerInput.error = getString(R.string.error_kilometer_required)
            valid = false
        }
        return valid
    }
}
