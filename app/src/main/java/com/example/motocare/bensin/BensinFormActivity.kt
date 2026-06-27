package com.example.motocare.bensin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.Bensin
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.ui.FormDialogHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class BensinFormActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private val priceRepository = FuelPriceRepository()
    private lateinit var motorText: TextView
    private lateinit var apiStatus: TextView
    private lateinit var dateInput: EditText
    private lateinit var typeInput: EditText
    private lateinit var brandInput: EditText
    private lateinit var octaneInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var literInput: EditText
    private lateinit var costInput: EditText
    private lateinit var kilometerInput: EditText
    private var bensinId: Long = 0
    private var motorId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bensin_form)

        dbHelper = MotoCareDbHelper(this)
        bensinId = intent.getLongExtra(EXTRA_BENSIN_ID, 0)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        bindViews()
        bindMotor()
        bindExisting()

        dateInput.setOnClickListener { showDatePicker() }
        brandInput.setOnClickListener { showBrandPicker() }
        octaneInput.setOnClickListener { showOctanePicker() }
        findViewById<Button>(R.id.buttonFetchFuelPrice).setOnClickListener { fetchPrice() }
        findViewById<Button>(R.id.buttonSaveBensin).setOnClickListener { saveBensin() }
        findViewById<Button>(R.id.buttonDeleteBensin).setOnClickListener { confirmDelete() }
    }

    private fun bindViews() {
        motorText = findViewById(R.id.textBensinMotor)
        apiStatus = findViewById(R.id.textFuelApiStatus)
        dateInput = findViewById(R.id.editBensinDate)
        typeInput = findViewById(R.id.editFuelType)
        brandInput = findViewById(R.id.editFuelBrand)
        octaneInput = findViewById(R.id.editFuelOctane)
        priceInput = findViewById(R.id.editFuelPrice)
        literInput = findViewById(R.id.editFuelLiter)
        costInput = findViewById(R.id.editFuelCost)
        kilometerInput = findViewById(R.id.editFuelKilometer)
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
        findViewById<Button>(R.id.buttonDeleteBensin).visibility =
            if (bensinId > 0) android.view.View.VISIBLE else android.view.View.GONE

        if (bensinId > 0) {
            dbHelper.getBensin(bensinId)?.let { bensin ->
                motorId = bensin.motorId
                dateInput.setText(bensin.fuelDate)
                typeInput.setText(bensin.fuelType)
                brandInput.setText(bensin.fuelBrand)
                octaneInput.setText(bensin.octane)
                priceInput.setText(bensin.pricePerLiter.toString())
                literInput.setText(bensin.liter.toString())
                costInput.setText(bensin.cost.toString())
                kilometerInput.setText(bensin.kilometer.toString())
            }
        } else {
            dateInput.setText(today())
            typeInput.setText(getString(R.string.default_fuel_type))
        }
    }

    private fun showDatePicker() {
        FormDialogHelper.showDatePicker(this, dateInput.text.toString()) {
            dateInput.setText(it)
        }
    }

    private fun showBrandPicker() {
        val brands = FUEL_OPTIONS.keys.toList()
        FormDialogHelper.showOptionPicker(this, getString(R.string.fuel_brand), brands) { brand ->
            brandInput.setText(brand)
            val octanes = FUEL_OPTIONS.getValue(brand)
            if (!octanes.contains(octaneInput.text.toString())) {
                octaneInput.setText(octanes.first())
            }
        }
    }

    private fun showOctanePicker() {
        val brand = brandInput.text.toString().ifBlank { getString(R.string.default_fuel_brand) }
        val octanes = FUEL_OPTIONS[brand] ?: FUEL_OPTIONS.getValue(getString(R.string.default_fuel_brand))
        FormDialogHelper.showOptionPicker(this, getString(R.string.fuel_octane), octanes) {
            octaneInput.setText(it)
        }
    }

    private fun fetchPrice() {
        val type = required(typeInput, R.string.error_fuel_type_required) ?: return
        val brand = required(brandInput, R.string.error_fuel_brand_required) ?: return
        val octane = required(octaneInput, R.string.error_fuel_octane_required) ?: return
        apiStatus.text = getString(R.string.fuel_api_loading)

        priceRepository.fetch(type, brand, octane) { result ->
            runOnUiThread {
                result.onSuccess { price ->
                    priceInput.setText(price.price.toString())
                    apiStatus.text = price.product
                }.onFailure { error ->
                    apiStatus.text = getString(R.string.fuel_api_failed_detail, error.message ?: "-")
                }
            }
        }
    }

    private fun saveBensin() {
        val date = required(dateInput, R.string.error_date_required) ?: return
        val type = required(typeInput, R.string.error_fuel_type_required) ?: return
        val brand = required(brandInput, R.string.error_fuel_brand_required) ?: return
        val octane = required(octaneInput, R.string.error_fuel_octane_required) ?: return
        val price = requiredInt(priceInput, R.string.error_fuel_price_required) ?: return
        val liter = requiredDouble(literInput, R.string.error_fuel_liter_required) ?: return
        val kilometer = requiredInt(kilometerInput, R.string.error_kilometer_required) ?: return
        val cost = costInput.text.toString().trim().toIntOrNull() ?: (price * liter).roundToInt()

        val bensin = Bensin(
            id = bensinId,
            motorId = motorId,
            fuelDate = date,
            fuelType = type,
            fuelBrand = brand,
            octane = octane,
            pricePerLiter = price,
            liter = liter,
            cost = cost,
            kilometer = kilometer
        )

        if (bensinId > 0) dbHelper.updateBensin(bensin) else dbHelper.insertBensin(bensin)
        Toast.makeText(this, R.string.bensin_saved, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_bensin_title)
            .setMessage(R.string.delete_bensin_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteBensin(bensinId)
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

    private fun requiredDouble(input: EditText, errorRes: Int): Double? {
        val value = input.text.toString().trim().toDoubleOrNull()
        if (value == null) input.error = getString(errorRes)
        return value
    }

    private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    companion object {
        const val EXTRA_BENSIN_ID = "extra_bensin_id"
        private val FUEL_OPTIONS = linkedMapOf(
            "Pertamina" to listOf("90", "92", "95", "98"),
            "Vivo" to listOf("92", "95"),
            "BP" to listOf("90", "92", "95"),
            "Shell" to listOf("92", "95", "98")
        )
    }
}
