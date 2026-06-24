package com.example.motocare.motor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Motor

class MotorFormActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var nameInput: EditText
    private lateinit var plateInput: EditText
    private lateinit var kilometerInput: EditText
    private var motorId: Long = 0
    private var currentMotor: Motor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motor_form)

        dbHelper = MotoCareDbHelper(this)
        motorId = intent.getLongExtra(EXTRA_MOTOR_ID, 0)
        nameInput = findViewById(R.id.editMotorName)
        plateInput = findViewById(R.id.editPlateNumber)
        kilometerInput = findViewById(R.id.editCurrentKilometer)

        if (motorId > 0) {
            currentMotor = dbHelper.getMotor(motorId)
            currentMotor?.let { motor ->
                nameInput.setText(motor.name)
                plateInput.setText(motor.plateNumber)
                kilometerInput.setText(motor.currentKilometer.toString())
            }
        }

        findViewById<Button>(R.id.buttonSaveMotor).setOnClickListener { saveMotor() }
    }

    private fun saveMotor() {
        val name = nameInput.text.toString().trim()
        val plate = plateInput.text.toString().trim()
        val kilometerText = kilometerInput.text.toString().trim()

        if (name.isEmpty()) {
            nameInput.error = getString(R.string.error_motor_name_required)
            return
        }
        if (plate.isEmpty()) {
            plateInput.error = getString(R.string.error_plate_number_required)
            return
        }
        val kilometer = kilometerText.toIntOrNull()
        if (kilometer == null) {
            kilometerInput.error = getString(R.string.error_kilometer_required)
            return
        }

        val motor = Motor(
            id = motorId,
            name = name,
            plateNumber = plate,
            currentKilometer = kilometer,
            isActive = currentMotor?.isActive ?: false
        )

        if (motorId > 0) {
            dbHelper.updateMotor(motor)
            Toast.makeText(this, R.string.motor_updated, Toast.LENGTH_SHORT).show()
        } else {
            dbHelper.insertMotor(motor)
            Toast.makeText(this, R.string.motor_saved, Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    companion object {
        const val EXTRA_MOTOR_ID = "extra_motor_id"
    }
}
