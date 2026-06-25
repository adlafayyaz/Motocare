package com.example.motocare.oli

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Oli
import com.example.motocare.ui.FormDialogHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OliFormActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var motorText: TextView
    private lateinit var dateInput: EditText
    private lateinit var typeInput: EditText
    private lateinit var kilometerInput: EditText
    private lateinit var intervalKmInput: EditText
    private lateinit var intervalMonthInput: EditText
    private lateinit var costInput: EditText
    private var oliId: Long = 0
    private var motorId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oli_form)

        dbHelper = MotoCareDbHelper(this)
        oliId = intent.getLongExtra(EXTRA_OLI_ID, 0)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        bindViews()
        bindMotor()
        bindExisting()

        dateInput.setOnClickListener {
            FormDialogHelper.showDatePicker(this, dateInput.text.toString()) { dateInput.setText(it) }
        }
        typeInput.setOnClickListener {
            FormDialogHelper.showOptionPicker(
                this,
                getString(R.string.oil_type),
                listOf("Oli mesin", "Oli gardan", "Filter oli")
            ) { typeInput.setText(it) }
        }
        findViewById<Button>(R.id.buttonSaveOli).setOnClickListener { saveOli() }
        findViewById<Button>(R.id.buttonDeleteOli).setOnClickListener { confirmDelete() }
    }

    private fun bindViews() {
        motorText = findViewById(R.id.textOliMotor)
        dateInput = findViewById(R.id.editOliDate)
        typeInput = findViewById(R.id.editOliType)
        kilometerInput = findViewById(R.id.editOliKilometer)
        intervalKmInput = findViewById(R.id.editOliIntervalKm)
        intervalMonthInput = findViewById(R.id.editOliIntervalMonth)
        costInput = findViewById(R.id.editOliCost)
    }

    private fun bindMotor() {
        val motor = dbHelper.getActiveMotor()
        if (motor == null) {
            Toast.makeText(this, R.string.add_motor_first, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        motorId = motor.id
        motorText.text = getString(R.string.active_motor_value, motor.name, motor.plateNumber)
    }

    private fun bindExisting() {
        findViewById<Button>(R.id.buttonDeleteOli).visibility =
            if (oliId > 0) android.view.View.VISIBLE else android.view.View.GONE

        if (oliId > 0) {
            dbHelper.getOli(oliId)?.let { oli ->
                motorId = oli.motorId
                dateInput.setText(oli.oilChangeDate)
                typeInput.setText(oli.oilType)
                kilometerInput.setText(oli.kilometer.toString())
                intervalKmInput.setText(oli.intervalKm.toString())
                intervalMonthInput.setText(oli.intervalMonth.toString())
                costInput.setText(oli.cost.toString())
            }
        } else {
            dateInput.setText(today())
            typeInput.setText(getString(R.string.default_oil_type))
            intervalKmInput.setText(DEFAULT_OIL_INTERVAL_KM.toString())
            intervalMonthInput.setText(DEFAULT_OIL_INTERVAL_MONTH.toString())
        }
    }

    private fun saveOli() {
        val date = required(dateInput, R.string.error_date_required) ?: return
        val type = required(typeInput, R.string.error_oil_type_required) ?: return
        val kilometer = requiredInt(kilometerInput, R.string.error_kilometer_required) ?: return
        val intervalKm = requiredInt(intervalKmInput, R.string.error_interval_required) ?: return
        val intervalMonth = requiredInt(intervalMonthInput, R.string.error_interval_required) ?: return
        val cost = requiredInt(costInput, R.string.error_cost_required) ?: return

        val oli = Oli(
            id = oliId,
            motorId = motorId,
            oilChangeDate = date,
            kilometer = kilometer,
            nextKilometer = if (intervalKm > kilometer) intervalKm else kilometer + intervalKm,
            intervalKm = intervalKm,
            intervalMonth = intervalMonth,
            oilType = type,
            cost = cost
        )

        if (oliId > 0) dbHelper.updateOli(oli) else dbHelper.insertOli(oli)
        Toast.makeText(this, R.string.oli_saved, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_oli_title)
            .setMessage(R.string.delete_oli_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteOli(oliId)
                finish()
            }
            .show()
    }

    private fun required(input: EditText, errorRes: Int): String? {
        val value = input.text.toString().trim()
        if (value.isEmpty()) {
            input.error = getString(errorRes)
            return null
        }
        return value
    }

    private fun requiredInt(input: EditText, errorRes: Int): Int? {
        val value = input.text.toString().trim().toIntOrNull()
        if (value == null) input.error = getString(errorRes)
        return value
    }

    private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    companion object {
        const val EXTRA_OLI_ID = "extra_oli_id"
        private const val DEFAULT_OIL_INTERVAL_KM = 2000
        private const val DEFAULT_OIL_INTERVAL_MONTH = 3
    }
}
