package com.example.motocare.setup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.DashboardActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Motor
import com.example.motocare.data.Oli
import com.example.motocare.data.Pajak
import com.example.motocare.data.Servis
import com.example.motocare.ui.FormDialogHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SetupMotorActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var motorNameInput: EditText
    private lateinit var plateNumberInput: EditText
    private lateinit var kilometerInput: EditText
    private lateinit var serviceCheck: CheckBox
    private lateinit var oilCheck: CheckBox
    private lateinit var taxCheck: CheckBox
    private lateinit var serviceGroup: View
    private lateinit var oilGroup: View
    private lateinit var taxGroup: View
    private lateinit var serviceKmInput: EditText
    private lateinit var serviceTargetInput: EditText
    private lateinit var oilKmInput: EditText
    private lateinit var oilTargetInput: EditText
    private lateinit var taxDueInput: EditText
    private lateinit var taxCostInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_motor)

        dbHelper = MotoCareDbHelper(this)
        motorNameInput = findViewById(R.id.editMotorName)
        plateNumberInput = findViewById(R.id.editPlateNumber)
        kilometerInput = findViewById(R.id.editCurrentKilometer)
        serviceCheck = findViewById(R.id.checkSetupService)
        oilCheck = findViewById(R.id.checkSetupOil)
        taxCheck = findViewById(R.id.checkSetupTax)
        serviceGroup = findViewById(R.id.groupSetupService)
        oilGroup = findViewById(R.id.groupSetupOil)
        taxGroup = findViewById(R.id.groupSetupTax)
        serviceKmInput = findViewById(R.id.editSetupServiceKm)
        serviceTargetInput = findViewById(R.id.editSetupServiceTarget)
        oilKmInput = findViewById(R.id.editSetupOilKm)
        oilTargetInput = findViewById(R.id.editSetupOilTarget)
        taxDueInput = findViewById(R.id.editSetupTaxDueDate)
        taxCostInput = findViewById(R.id.editSetupTaxCost)

        bindOptionalSetup()

        findViewById<Button>(R.id.buttonSaveMotor).setOnClickListener {
            if (isValid()) {
                val motorId = dbHelper.insertMotor(
                    Motor(
                        name = motorNameInput.text.toString().trim(),
                        plateNumber = plateNumberInput.text.toString().trim(),
                        currentKilometer = kilometerInput.text.toString().trim().toInt(),
                        isActive = true
                    )
                )
                saveOptionalRecords(motorId)
                Toast.makeText(this, R.string.setup_motor_saved, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }
    }

    private fun bindOptionalSetup() {
        serviceCheck.setOnCheckedChangeListener { _, checked ->
            serviceGroup.visibility = if (checked) View.VISIBLE else View.GONE
            if (checked) fillKmIfEmpty(serviceKmInput)
        }
        oilCheck.setOnCheckedChangeListener { _, checked ->
            oilGroup.visibility = if (checked) View.VISIBLE else View.GONE
            if (checked) fillKmIfEmpty(oilKmInput)
        }
        taxCheck.setOnCheckedChangeListener { _, checked ->
            taxGroup.visibility = if (checked) View.VISIBLE else View.GONE
            if (checked && taxDueInput.text.isBlank()) taxDueInput.setText(today())
        }
        taxDueInput.setOnClickListener {
            FormDialogHelper.showDatePicker(this, taxDueInput.text.toString()) { taxDueInput.setText(it) }
        }
        serviceKmInput.addTextChangedListener(targetWatcher(serviceKmInput, serviceTargetInput, SERVICE_INTERVAL_KM))
        oilKmInput.addTextChangedListener(targetWatcher(oilKmInput, oilTargetInput, OIL_INTERVAL_KM))
    }

    private fun fillKmIfEmpty(input: EditText) {
        if (input.text.isBlank()) input.setText(kilometerInput.text.toString().trim())
    }

    private fun targetWatcher(source: EditText, target: EditText, interval: Int): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (target.text.isBlank()) {
                    val km = source.text.toString().trim().toIntOrNull() ?: return
                    target.setText((km + interval).toString())
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
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
        if (serviceCheck.isChecked) {
            valid = validateNumber(serviceKmInput, R.string.error_kilometer_required) && valid
            valid = validateNumber(serviceTargetInput, R.string.error_interval_required) && valid
        }
        if (oilCheck.isChecked) {
            valid = validateNumber(oilKmInput, R.string.error_kilometer_required) && valid
            valid = validateNumber(oilTargetInput, R.string.error_interval_required) && valid
        }
        if (taxCheck.isChecked) {
            if (taxDueInput.text.isBlank()) {
                taxDueInput.error = getString(R.string.error_date_required)
                valid = false
            }
            if (taxCostInput.text.isNotBlank() && taxCostInput.text.toString().toIntOrNull() == null) {
                taxCostInput.error = getString(R.string.error_cost_number)
                valid = false
            }
        }
        return valid
    }

    private fun validateNumber(input: EditText, errorRes: Int): Boolean {
        return if (input.text.toString().trim().toIntOrNull() == null) {
            input.error = getString(errorRes)
            false
        } else {
            true
        }
    }

    private fun saveOptionalRecords(motorId: Long) {
        if (serviceCheck.isChecked) {
            val kilometer = serviceKmInput.text.toString().trim().toInt()
            val target = serviceTargetInput.text.toString().trim().toInt()
            dbHelper.insertServis(
                Servis(
                    motorId = motorId,
                    serviceDate = today(),
                    serviceType = getString(R.string.setup_initial_record),
                    kilometer = kilometer,
                    intervalKm = target,
                    intervalMonth = 6,
                    cost = 0,
                    note = getString(R.string.setup_initial_record)
                )
            )
        }
        if (oilCheck.isChecked) {
            val kilometer = oilKmInput.text.toString().trim().toInt()
            val target = oilTargetInput.text.toString().trim().toInt()
            dbHelper.insertOli(
                Oli(
                    motorId = motorId,
                    oilChangeDate = today(),
                    kilometer = kilometer,
                    nextKilometer = target,
                    intervalKm = target,
                    intervalMonth = 3,
                    oilType = getString(R.string.setup_initial_record),
                    cost = 0
                )
            )
        }
        if (taxCheck.isChecked) {
            dbHelper.insertPajak(
                Pajak(
                    motorId = motorId,
                    taxType = "STNK tahunan",
                    dueDate = taxDueInput.text.toString().trim(),
                    cost = taxCostInput.text.toString().trim().toIntOrNull() ?: 0,
                    status = "Belum bayar"
                )
            )
        }
    }

    private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    private companion object {
        const val SERVICE_INTERVAL_KM = 4000
        const val OIL_INTERVAL_KM = 3000
    }
}
