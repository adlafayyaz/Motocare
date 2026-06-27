package com.example.motocare.pajak

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
import com.example.motocare.data.Pajak
import com.example.motocare.ui.FormDialogHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PajakFormActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var motorText: TextView
    private lateinit var typeInput: EditText
    private lateinit var dueDateInput: EditText
    private lateinit var costInput: EditText
    private lateinit var statusInput: EditText
    private var pajakId: Long = 0
    private var motorId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pajak_form)

        dbHelper = MotoCareDbHelper(this)
        pajakId = intent.getLongExtra(EXTRA_PAJAK_ID, 0)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        motorText = findViewById(R.id.textPajakMotor)
        typeInput = findViewById(R.id.editPajakType)
        dueDateInput = findViewById(R.id.editPajakDueDate)
        costInput = findViewById(R.id.editPajakCost)
        statusInput = findViewById(R.id.editPajakStatus)
        bindMotor()
        bindExisting()

        dueDateInput.setOnClickListener {
            FormDialogHelper.showDatePicker(this, dueDateInput.text.toString()) { dueDateInput.setText(it) }
        }
        typeInput.setOnClickListener {
            FormDialogHelper.showOptionPicker(
                this,
                getString(R.string.tax_type),
                listOf(getString(R.string.default_tax_type), "Pajak 5 tahunan", "Balik nama", "Denda pajak")
            ) { typeInput.setText(it) }
        }
        statusInput.setOnClickListener {
            FormDialogHelper.showOptionPicker(
                this,
                getString(R.string.tax_status),
                listOf(getString(R.string.default_tax_status), "Lunas", "Aktif")
            ) { statusInput.setText(it) }
        }
        findViewById<Button>(R.id.buttonSavePajak).setOnClickListener { savePajak() }
        findViewById<Button>(R.id.buttonDeletePajak).setOnClickListener { confirmDelete() }
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
        findViewById<Button>(R.id.buttonDeletePajak).visibility =
            if (pajakId > 0) android.view.View.VISIBLE else android.view.View.GONE

        if (pajakId > 0) {
            dbHelper.getPajak(pajakId)?.let { pajak ->
                motorId = pajak.motorId
                typeInput.setText(pajak.taxType)
                dueDateInput.setText(pajak.dueDate)
                costInput.setText(pajak.cost.toString())
                statusInput.setText(pajak.status)
            }
        } else {
            dueDateInput.setText(SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()))
        }
    }

    private fun savePajak() {
        val taxType = required(typeInput, R.string.error_tax_type_required) ?: return
        val dueDate = required(dueDateInput, R.string.error_date_required) ?: return
        val cost = requiredInt(costInput, R.string.error_cost_required) ?: return
        val status = required(statusInput, R.string.error_tax_status_required) ?: return

        val pajak = Pajak(
            id = pajakId,
            motorId = motorId,
            taxType = taxType,
            dueDate = dueDate,
            cost = cost,
            status = status
        )

        if (pajakId > 0) dbHelper.updatePajak(pajak) else dbHelper.insertPajak(pajak)
        Toast.makeText(this, R.string.pajak_saved, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_pajak_title)
            .setMessage(R.string.delete_pajak_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deletePajak(pajakId)
                finish()
            }
            .show()
    }

    private fun required(input: EditText, errorRes: Int): String? {
        val value = input.text.toString().trim()
        if (value.isEmpty()) input.error = getString(errorRes)
        return value.ifEmpty { null }
    }

    private fun requiredInt(input: EditText, errorRes: Int): Int? {
        val value = input.text.toString().trim().toIntOrNull()
        if (value == null) input.error = getString(errorRes)
        return value
    }

    companion object {
        const val EXTRA_PAJAK_ID = "extra_pajak_id"
    }
}
