package com.example.motocare.setup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var titleText: TextView
    private lateinit var bodyText: TextView
    private lateinit var primaryButton: Button
    private lateinit var skipButton: TextView
    private lateinit var motorGroup: View
    private lateinit var serviceGroup: View
    private lateinit var oilGroup: View
    private lateinit var taxGroup: View
    private lateinit var infoCard: View
    private lateinit var infoTitle: TextView
    private lateinit var infoBody: TextView
    private lateinit var serviceKmInput: EditText
    private lateinit var serviceTargetInput: EditText
    private lateinit var serviceRecommendationText: TextView
    private lateinit var serviceDateInput: EditText
    private lateinit var serviceMonthsInput: EditText
    private lateinit var oilKmInput: EditText
    private lateinit var oilTargetInput: EditText
    private lateinit var oilRecommendationText: TextView
    private lateinit var oilDateInput: EditText
    private lateinit var oilMonthsInput: EditText
    private lateinit var taxDueInput: EditText
    private lateinit var taxCostInput: EditText
    private lateinit var dots: List<View>
    private var setupStep = Step.MOTOR
    private var serviceSkipped = false
    private var oilSkipped = false
    private var taxSkipped = false
    private var serviceTargetEditedByUser = false
    private var oilTargetEditedByUser = false
    private var updatingTarget = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_wizard)

        dbHelper = MotoCareDbHelper(this)
        titleText = findViewById(R.id.textSetupTitle)
        bodyText = findViewById(R.id.textSetupBody)
        primaryButton = findViewById(R.id.buttonSetupPrimary)
        skipButton = findViewById(R.id.buttonSetupSkip)
        motorNameInput = findViewById(R.id.editMotorName)
        plateNumberInput = findViewById(R.id.editPlateNumber)
        kilometerInput = findViewById(R.id.editCurrentKilometer)
        motorGroup = findViewById(R.id.groupSetupMotor)
        serviceGroup = findViewById(R.id.groupSetupService)
        oilGroup = findViewById(R.id.groupSetupOil)
        taxGroup = findViewById(R.id.groupSetupTax)
        infoCard = findViewById(R.id.cardSetupInfo)
        infoTitle = findViewById(R.id.textInfoTitle)
        infoBody = findViewById(R.id.textInfoBody)
        serviceKmInput = findViewById(R.id.editSetupServiceKm)
        serviceTargetInput = findViewById(R.id.editSetupServiceTarget)
        serviceRecommendationText = findViewById(R.id.textSetupServiceRecommendation)
        serviceDateInput = findViewById(R.id.editSetupServiceDate)
        serviceMonthsInput = findViewById(R.id.editSetupServiceMonths)
        oilKmInput = findViewById(R.id.editSetupOilKm)
        oilTargetInput = findViewById(R.id.editSetupOilTarget)
        oilRecommendationText = findViewById(R.id.textSetupOilRecommendation)
        oilDateInput = findViewById(R.id.editSetupOilDate)
        oilMonthsInput = findViewById(R.id.editSetupOilMonths)
        taxDueInput = findViewById(R.id.editSetupTaxDueDate)
        taxCostInput = findViewById(R.id.editSetupTaxCost)
        dots = listOf(
            findViewById(R.id.dotSetupMotor),
            findViewById(R.id.dotSetupService),
            findViewById(R.id.dotSetupOil),
            findViewById(R.id.dotSetupTax)
        )

        bindOptionalSetup()
        renderStep()

        primaryButton.setOnClickListener {
            when (setupStep) {
                Step.MOTOR -> if (isMotorValid()) moveTo(Step.SERVICE)
                Step.SERVICE -> if (isOptionalServiceValid()) moveTo(Step.OIL)
                Step.OIL -> if (isOptionalOilValid()) moveTo(Step.TAX)
                Step.TAX -> if (isOptionalTaxValid()) finishSetup()
            }
        }
        skipButton.setOnClickListener {
            when (setupStep) {
                Step.SERVICE -> {
                    serviceSkipped = true
                    moveTo(Step.OIL)
                }
                Step.OIL -> {
                    oilSkipped = true
                    moveTo(Step.TAX)
                }
                Step.TAX -> {
                    taxSkipped = true
                    finishSetup()
                }
                Step.MOTOR -> Unit
            }
        }
    }

    private fun bindOptionalSetup() {
        serviceDateInput.setOnClickListener {
            FormDialogHelper.showDatePicker(this, serviceDateInput.text.toString()) { serviceDateInput.setText(it) }
        }
        oilDateInput.setOnClickListener {
            FormDialogHelper.showDatePicker(this, oilDateInput.text.toString()) { oilDateInput.setText(it) }
        }
        taxDueInput.setOnClickListener {
            FormDialogHelper.showDatePicker(this, taxDueInput.text.toString()) { taxDueInput.setText(it) }
        }
        serviceKmInput.addTextChangedListener(targetWatcher(
            source = serviceKmInput,
            target = serviceTargetInput,
            recommendation = serviceRecommendationText,
            interval = SERVICE_INTERVAL_KM,
            recommendationRes = R.string.service_recommendation_info,
            isEditedByUser = { serviceTargetEditedByUser }
        ))
        oilKmInput.addTextChangedListener(targetWatcher(
            source = oilKmInput,
            target = oilTargetInput,
            recommendation = oilRecommendationText,
            interval = OIL_INTERVAL_KM,
            recommendationRes = R.string.oil_recommendation_info,
            isEditedByUser = { oilTargetEditedByUser }
        ))
        serviceTargetInput.addTextChangedListener(manualTargetWatcher { serviceTargetEditedByUser = true })
        oilTargetInput.addTextChangedListener(manualTargetWatcher { oilTargetEditedByUser = true })
    }

    private fun moveTo(step: Step) {
        setupStep = step
        renderStep()
    }

    private fun renderStep() {
        motorGroup.visibility = if (setupStep == Step.MOTOR) View.VISIBLE else View.GONE
        serviceGroup.visibility = if (setupStep == Step.SERVICE) View.VISIBLE else View.GONE
        oilGroup.visibility = if (setupStep == Step.OIL) View.VISIBLE else View.GONE
        taxGroup.visibility = if (setupStep == Step.TAX) View.VISIBLE else View.GONE
        infoCard.visibility = if (setupStep == Step.MOTOR) View.GONE else View.VISIBLE
        skipButton.visibility = if (setupStep == Step.MOTOR) View.GONE else View.VISIBLE
        primaryButton.text = getString(if (setupStep == Step.TAX) R.string.setup_finish else R.string.next)

        when (setupStep) {
            Step.MOTOR -> {
                titleText.text = getString(R.string.setup_motor_title)
                bodyText.text = getString(R.string.setup_motor_body)
            }
            Step.SERVICE -> {
                titleText.text = getString(R.string.setup_service_title)
                bodyText.text = getString(R.string.setup_service_body)
                infoTitle.text = getString(R.string.setup_schedule_title)
                infoBody.text = getString(R.string.setup_schedule_body)
            }
            Step.OIL -> {
                titleText.text = getString(R.string.setup_oil_title)
                bodyText.text = getString(R.string.setup_oil_body)
                infoTitle.text = getString(R.string.setup_reminder_title)
                infoBody.text = getString(R.string.setup_reminder_body)
            }
            Step.TAX -> {
                titleText.text = getString(R.string.setup_tax_page_title)
                bodyText.text = getString(R.string.setup_tax_page_body)
                infoTitle.text = getString(R.string.setup_tax_title)
                infoBody.text = getString(R.string.setup_tax_body)
            }
        }
        updateDots()
    }

    private fun updateDots() {
        dots.forEachIndexed { index, dot ->
            val active = index == setupStep.ordinal
            dot.setBackgroundResource(if (active) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive)
            dot.layoutParams = dot.layoutParams.apply {
                width = resources.getDimensionPixelSize(if (active) R.dimen.setup_dot_active_width else R.dimen.setup_dot_size)
                height = resources.getDimensionPixelSize(R.dimen.setup_dot_size)
            }
        }
    }

    private fun targetWatcher(
        source: EditText,
        target: EditText,
        recommendation: TextView,
        interval: Int,
        recommendationRes: Int,
        isEditedByUser: () -> Boolean
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val km = source.text.toString().trim().toIntOrNull()
                if (km == null) {
                    recommendation.visibility = View.GONE
                    return
                }
                val recommendedTarget = km + interval
                recommendation.text = getString(recommendationRes, recommendedTarget)
                recommendation.visibility = View.VISIBLE
                if (!isEditedByUser()) {
                    updatingTarget = true
                    target.setText(recommendedTarget.toString())
                    target.setSelection(target.text.length)
                    updatingTarget = false
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
    }

    private fun manualTargetWatcher(onUserEdit: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!updatingTarget) onUserEdit()
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
    }

    private fun isMotorValid(): Boolean {
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

    private fun isOptionalServiceValid(): Boolean {
        serviceSkipped = false
        var valid = true
        valid = validateNumber(serviceKmInput, R.string.error_kilometer_required) && valid
        valid = validateNumber(serviceTargetInput, R.string.error_interval_required) && valid
        valid = validateNumber(serviceMonthsInput, R.string.error_interval_required) && valid
        if (serviceDateInput.text.isBlank()) {
            serviceDateInput.error = getString(R.string.error_date_required)
            valid = false
        }
        return valid
    }

    private fun isOptionalOilValid(): Boolean {
        oilSkipped = false
        var valid = true
        valid = validateNumber(oilKmInput, R.string.error_kilometer_required) && valid
        valid = validateNumber(oilTargetInput, R.string.error_interval_required) && valid
        valid = validateNumber(oilMonthsInput, R.string.error_interval_required) && valid
        if (oilDateInput.text.isBlank()) {
            oilDateInput.error = getString(R.string.error_date_required)
            valid = false
        }
        return valid
    }

    private fun isOptionalTaxValid(): Boolean {
        taxSkipped = false
        var valid = true
        if (taxDueInput.text.isBlank()) {
            taxDueInput.error = getString(R.string.error_date_required)
            valid = false
        }
        if (taxCostInput.text.isNotBlank() && taxCostInput.text.toString().toIntOrNull() == null) {
            taxCostInput.error = getString(R.string.error_cost_number)
            valid = false
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

    private fun finishSetup() {
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

    private fun saveOptionalRecords(motorId: Long) {
        if (!serviceSkipped) {
            val kilometer = serviceKmInput.text.toString().trim().toInt()
            val target = serviceTargetInput.text.toString().trim().toInt()
            dbHelper.insertServis(
                Servis(
                    motorId = motorId,
                    serviceDate = serviceDateInput.text.toString().trim(),
                    serviceType = getString(R.string.setup_initial_record),
                    kilometer = kilometer,
                    intervalKm = target,
                    intervalMonth = serviceMonthsInput.text.toString().trim().toInt(),
                    cost = 0,
                    note = getString(R.string.setup_initial_record)
                )
            )
        }
        if (!oilSkipped) {
            val kilometer = oilKmInput.text.toString().trim().toInt()
            val target = oilTargetInput.text.toString().trim().toInt()
            dbHelper.insertOli(
                Oli(
                    motorId = motorId,
                    oilChangeDate = oilDateInput.text.toString().trim(),
                    kilometer = kilometer,
                    nextKilometer = target,
                    intervalKm = target,
                    intervalMonth = oilMonthsInput.text.toString().trim().toInt(),
                    oilType = getString(R.string.setup_initial_record),
                    cost = 0
                )
            )
        }
        if (!taxSkipped) {
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

    private enum class Step {
        MOTOR,
        SERVICE,
        OIL,
        TAX
    }

    private companion object {
        const val SERVICE_INTERVAL_KM = 4000
        const val OIL_INTERVAL_KM = 3000
    }
}
