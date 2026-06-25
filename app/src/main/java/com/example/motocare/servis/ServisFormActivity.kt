package com.example.motocare.servis

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
import com.example.motocare.data.Servis
import com.example.motocare.ui.FormDialogHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ServisFormActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var motorText: TextView
    private lateinit var dateInput: EditText
    private lateinit var typeInput: EditText
    private lateinit var kilometerInput: EditText
    private lateinit var intervalKmInput: EditText
    private lateinit var intervalMonthInput: EditText
    private lateinit var costInput: EditText
    private lateinit var noteInput: EditText
    private lateinit var typeError: TextView
    private lateinit var kilometerError: TextView
    private lateinit var costError: TextView
    private var servisId: Long = 0
    private var motorId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servis_form)

        dbHelper = MotoCareDbHelper(this)
        servisId = intent.getLongExtra(EXTRA_SERVIS_ID, 0)
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
                getString(R.string.service_type),
                listOf("Tune up", "Cek rem", "Ganti kampas", "Servis rutin")
            ) { typeInput.setText(it) }
        }
        findViewById<Button>(R.id.buttonSaveServis).setOnClickListener { saveServis() }
        findViewById<Button>(R.id.buttonDeleteServis).setOnClickListener { confirmDelete() }
    }

    private fun bindViews() {
        motorText = findViewById(R.id.textServisMotor)
        dateInput = findViewById(R.id.editServisDate)
        typeInput = findViewById(R.id.editServisType)
        kilometerInput = findViewById(R.id.editServisKilometer)
        intervalKmInput = findViewById(R.id.editServisIntervalKm)
        intervalMonthInput = findViewById(R.id.editServisIntervalMonth)
        costInput = findViewById(R.id.editServisCost)
        noteInput = findViewById(R.id.editServisNote)
        typeError = findViewById(R.id.errorServisType)
        kilometerError = findViewById(R.id.errorServisKilometer)
        costError = findViewById(R.id.errorServisCost)
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
        findViewById<Button>(R.id.buttonDeleteServis).visibility =
            if (servisId > 0) android.view.View.VISIBLE else android.view.View.GONE

        if (servisId > 0) {
            dbHelper.getServis(servisId)?.let { servis ->
                motorId = servis.motorId
                dateInput.setText(servis.serviceDate)
                typeInput.setText(servis.serviceType)
                kilometerInput.setText(servis.kilometer.toString())
                intervalKmInput.setText(servis.intervalKm.toString())
                intervalMonthInput.setText(servis.intervalMonth.toString())
                costInput.setText(servis.cost.toString())
                noteInput.setText(servis.note)
            }
        } else {
            dateInput.setText(today())
            typeInput.setText(getString(R.string.default_service_type))
            intervalKmInput.setText(DEFAULT_SERVICE_INTERVAL_KM.toString())
            intervalMonthInput.setText(DEFAULT_SERVICE_INTERVAL_MONTH.toString())
        }
    }

    private fun saveServis() {
        clearInputErrors()
        val date = required(dateInput, R.string.error_date_required) ?: return
        val type = required(typeInput, R.string.error_service_type_required, typeError) ?: return
        val kilometer = requiredInt(kilometerInput, R.string.error_kilometer_required, kilometerError) ?: return
        val intervalKm = requiredInt(intervalKmInput, R.string.error_interval_required) ?: return
        val intervalMonth = requiredInt(intervalMonthInput, R.string.error_interval_required) ?: return
        val cost = requiredInt(costInput, R.string.error_cost_number, costError) ?: return

        val servis = Servis(
            id = servisId,
            motorId = motorId,
            serviceDate = date,
            serviceType = type,
            kilometer = kilometer,
            intervalKm = intervalKm,
            intervalMonth = intervalMonth,
            cost = cost,
            note = noteInput.text.toString().trim()
        )

        if (servisId > 0) dbHelper.updateServis(servis) else dbHelper.insertServis(servis)
        Toast.makeText(this, R.string.servis_saved, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_servis_title)
            .setMessage(R.string.delete_servis_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteServis(servisId)
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

    private fun required(input: EditText, errorRes: Int, errorView: TextView): String? {
        val value = input.text.toString().trim()
        if (value.isEmpty()) {
            showInputError(input, errorView, getString(errorRes))
            return null
        }
        return value
    }

    private fun requiredInt(input: EditText, errorRes: Int): Int? {
        val value = input.text.toString().trim().toIntOrNull()
        if (value == null) input.error = getString(errorRes)
        return value
    }

    private fun requiredInt(input: EditText, errorRes: Int, errorView: TextView): Int? {
        val raw = input.text.toString().trim()
        val value = raw.toIntOrNull()
        if (raw.isEmpty() || value == null) {
            showInputError(input, errorView, getString(errorRes))
            return null
        }
        return value
    }

    private fun showInputError(input: EditText, errorView: TextView, message: String) {
        input.setBackgroundResource(R.drawable.bg_input_error)
        errorView.text = message
        errorView.visibility = View.VISIBLE
    }

    private fun clearInputErrors() {
        listOf(typeInput, kilometerInput, costInput).forEach {
            it.setBackgroundResource(R.drawable.bg_input_dark)
        }
        listOf(typeError, kilometerError, costError).forEach {
            it.visibility = View.GONE
        }
    }

    private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    companion object {
        const val EXTRA_SERVIS_ID = "extra_servis_id"
        private const val DEFAULT_SERVICE_INTERVAL_KM = 3000
        private const val DEFAULT_SERVICE_INTERVAL_MONTH = 6
    }
}
