package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.bensin.BensinListActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Motor
import com.example.motocare.data.Pajak
import com.example.motocare.navigation.BottomNavBinder
import com.example.motocare.oli.OliListActivity
import com.example.motocare.pajak.PajakListActivity
import com.example.motocare.profile.ProfileAvatarLoader
import com.example.motocare.profile.ProfileStore
import com.example.motocare.servis.ServisListActivity
import com.example.motocare.ui.DashboardDonutView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.ceil

class DashboardActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private var costMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        dbHelper = MotoCareDbHelper(this)

        findViewById<Button>(R.id.buttonDashboardAddMotor).setOnClickListener {
            openNoAnim(com.example.motocare.motor.MotorFormActivity::class.java)
        }
        findViewById<View>(R.id.buttonDashboardProfile).setOnClickListener {
            openNoAnim(com.example.motocare.profile.ProfileActivity::class.java)
        }
        findViewById<View>(R.id.actionDashboardServis).setOnClickListener { openNoAnim(ServisListActivity::class.java) }
        findViewById<View>(R.id.actionDashboardOli).setOnClickListener { openNoAnim(OliListActivity::class.java) }
        findViewById<View>(R.id.actionDashboardPajak).setOnClickListener { openNoAnim(PajakListActivity::class.java) }
        findViewById<View>(R.id.actionDashboardBensin).setOnClickListener { openNoAnim(BensinListActivity::class.java) }
        findViewById<TextView>(R.id.buttonCostTab).setOnClickListener { selectCostMode(true) }
        findViewById<TextView>(R.id.buttonDistanceTab).setOnClickListener { selectCostMode(false) }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_HOME)
    }

    override fun onResume() {
        super.onResume()
        bindDashboard()
    }

    private fun bindDashboard() {
        val activeMotor = dbHelper.getActiveMotor()
        val hasMotor = activeMotor != null
        val profile = bindProfile()

        findViewById<TextView>(R.id.textDashboardGreeting).text = getString(
            R.string.dashboard_greeting_name,
            profile.getName()
        )
        findViewById<TextView>(R.id.textDashboardMotorName).text = activeMotor?.name
            ?: getString(R.string.no_active_motor)
        findViewById<TextView>(R.id.textDashboardMotorPlate).text = activeMotor?.plateNumber
            ?: getString(R.string.add_motor_first)
        findViewById<TextView>(R.id.textDashboardKilometer).text = activeMotor?.let {
            getString(R.string.motor_kilometer_value, it.currentKilometer)
        } ?: "-"

        val fuelTotal = dbHelper.getFuelMonthlyTotal()
        val serviceTotal = dbHelper.getServiceMonthlyTotal()
        val oilTotal = dbHelper.getOilMonthlyTotal()
        val taxTotal = dbHelper.getTaxMonthlyTotal()
        bindMetricCard(
            activeMotor = activeMotor,
            fuelTotal = fuelTotal,
            serviceTotal = serviceTotal,
            oilTotal = oilTotal,
            taxTotal = taxTotal
        )

        if (activeMotor == null) {
            bindEmptyEstimate()
        } else {
            bindServiceEstimate(activeMotor.id, activeMotor.currentKilometer)
            bindOilEstimate(activeMotor.id, activeMotor.currentKilometer)
            bindTaxEstimate(activeMotor.id)
        }
        findViewById<Button>(R.id.buttonDashboardAddMotor).visibility =
            if (hasMotor) View.GONE else View.VISIBLE
    }

    private fun bindProfile(): ProfileStore {
        val store = ProfileStore(this)
        FirebaseAuth.getInstance().currentUser?.let { user ->
            store.saveGoogleProfile(user.displayName, user.email, user.photoUrl?.toString())
        }
        val image = findViewById<ImageView>(R.id.imageDashboardProfile)
        ProfileAvatarLoader.load(image, store.getAvatarUri())
        return store
    }

    private fun selectCostMode(showCost: Boolean) {
        costMode = showCost
        val cost = findViewById<TextView>(R.id.buttonCostTab)
        val distance = findViewById<TextView>(R.id.buttonDistanceTab)
        cost.setBackgroundResource(if (showCost) R.drawable.bg_segment_active else 0)
        distance.setBackgroundResource(if (showCost) 0 else R.drawable.bg_segment_active)
        cost.setTextColor(getColor(if (showCost) R.color.motocare_text else R.color.motocare_muted))
        distance.setTextColor(getColor(if (showCost) R.color.motocare_muted else R.color.motocare_text))
        bindDashboard()
        val active = if (showCost) cost else distance
        active.animate().scaleX(1.03f).scaleY(1.03f).setDuration(90).withEndAction {
            active.animate().scaleX(1f).scaleY(1f).setDuration(90).start()
        }.start()
    }

    private fun openNoAnim(target: Class<out AppCompatActivity>) {
        startActivity(Intent(this, target))
        overridePendingTransition(0, 0)
    }

    private fun bindEmptyEstimate() {
        findViewById<TextView>(R.id.textNextService).text = getString(R.string.next_service_title)
        findViewById<TextView>(R.id.textNextServiceMeta).text = getString(R.string.no_motor_estimate)
        findViewById<TextView>(R.id.textNextServiceValue).text = "-"
        findViewById<TextView>(R.id.textNextServiceLabel).text = getString(R.string.target_label)
    }

    private fun bindMetricCard(
        activeMotor: Motor?,
        fuelTotal: Int,
        serviceTotal: Int,
        oilTotal: Int,
        taxTotal: Int
    ) {
        if (costMode) {
            findViewById<TextView>(R.id.textDashboardMetricTitle).text = getString(R.string.monthly_expense)
            findViewById<TextView>(R.id.textMonthlyTotal).text = getString(
                R.string.rupiah_value,
                fuelTotal + serviceTotal + oilTotal + taxTotal
            )
            findViewById<TextView>(R.id.textTransactionCount).text = getString(
                R.string.transactions_count_value,
                dbHelper.getRecordCount()
            )
            findViewById<TextView>(R.id.textServiceTotal).text = getString(R.string.rupiah_value, serviceTotal)
            findViewById<TextView>(R.id.textOilTotal).text = getString(R.string.rupiah_value, oilTotal)
            findViewById<TextView>(R.id.textTaxTotal).text = getString(R.string.rupiah_value, taxTotal)
            findViewById<TextView>(R.id.textFuelTotal).text = getString(R.string.rupiah_value, fuelTotal)
            findViewById<DashboardDonutView>(R.id.dashboardDonut).setData(
                fuel = fuelTotal,
                service = serviceTotal,
                oil = oilTotal,
                tax = taxTotal
            )
            return
        }

        val serviceKm = activeMotor?.let { serviceRemainingKm(it.id, it.currentKilometer) }
        val oilKm = activeMotor?.let { oilRemainingKm(it.id, it.currentKilometer) }
        val taxDays = activeMotor?.let { taxDays(it.id) }
        val fuelLiters = activeMotor?.let { fuelLiters(it.id) }

        findViewById<TextView>(R.id.textDashboardMetricTitle).text = getString(R.string.distance_overview)
        findViewById<TextView>(R.id.textMonthlyTotal).text = activeMotor?.let {
            getString(R.string.km_value_short, it.currentKilometer)
        } ?: "-"
        findViewById<TextView>(R.id.textTransactionCount).text = getString(R.string.active_motor_distance)
        findViewById<TextView>(R.id.textServiceTotal).text = serviceKm?.let {
            getString(R.string.km_remaining_value, it)
        } ?: "-"
        findViewById<TextView>(R.id.textOilTotal).text = oilKm?.let {
            getString(R.string.km_remaining_value, it)
        } ?: "-"
        findViewById<TextView>(R.id.textTaxTotal).text = taxDays?.let {
            getString(R.string.days_value, it)
        } ?: "-"
        findViewById<TextView>(R.id.textFuelTotal).text = fuelLiters?.let {
            getString(R.string.liter_value_short, it)
        } ?: "-"
        findViewById<DashboardDonutView>(R.id.dashboardDonut).setData(
            fuel = ((fuelLiters ?: 0.0) * 10).toInt(),
            service = serviceKm ?: 0,
            oil = oilKm ?: 0,
            tax = taxDays ?: 0
        )
    }

    private fun bindServiceEstimate(motorId: Long, currentKilometer: Int) {
        val latest = dbHelper.getLatestServis(motorId)
        findViewById<TextView>(R.id.textNextService).text = getString(R.string.next_service_title)
        if (latest == null) {
            findViewById<TextView>(R.id.textNextServiceMeta).text = getString(R.string.no_service_data_short)
            findViewById<TextView>(R.id.textNextServiceValue).text = "-"
            findViewById<TextView>(R.id.textNextServiceLabel).text = getString(R.string.target_label)
            return
        }

        val targetKm = serviceTargetKm(latest.kilometer, latest.intervalKm)
        val remainingKm = (targetKm - latest.kilometer).coerceAtLeast(0)
        findViewById<TextView>(R.id.textNextServiceMeta).text = getString(
            R.string.service_remaining_meta,
            remainingKm
        )
        findViewById<TextView>(R.id.textNextServiceValue).text = getString(
            R.string.km_value_short,
            targetKm
        )
        findViewById<TextView>(R.id.textNextServiceLabel).text = getString(R.string.target_label)
    }

    private fun bindOilEstimate(motorId: Long, currentKilometer: Int) {
        val latest = dbHelper.getLatestOli(motorId)
        findViewById<TextView>(R.id.textOilTotal).text = if (latest == null) {
            "-"
        } else {
            val remainingKm = (latest.nextKilometer - currentKilometer).coerceAtLeast(0)
            getString(R.string.km_remaining_value, remainingKm)
        }
    }

    private fun bindTaxEstimate(motorId: Long) {
        val nearest = dbHelper.getPajakByMotor(motorId).firstOrNull { it.status.equals("Belum bayar", true) }
            ?: dbHelper.getPajakByMotor(motorId).firstOrNull()
        findViewById<TextView>(R.id.textTaxTotal).text = nearest?.let { pajak ->
            val days = daysUntil(pajak)
            if (days == null) getString(R.string.rupiah_value, pajak.cost) else getString(R.string.days_value, days)
        } ?: "-"
    }

    private fun daysUntil(pajak: Pajak): Int? {
        return runCatching {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val due = format.parse(pajak.dueDate) ?: return null
            val today = format.parse(format.format(System.currentTimeMillis())) ?: return null
            val millis = due.time - today.time
            ceil(millis / DAY_MILLIS.toDouble()).toInt()
        }.getOrNull()
    }

    private fun serviceRemainingKm(motorId: Long, currentKilometer: Int): Int? {
        val latest = dbHelper.getLatestServis(motorId) ?: return null
        return (serviceTargetKm(latest.kilometer, latest.intervalKm) - latest.kilometer).coerceAtLeast(0)
    }

    private fun oilRemainingKm(motorId: Long, currentKilometer: Int): Int? {
        val latest = dbHelper.getLatestOli(motorId) ?: return null
        return (latest.nextKilometer - currentKilometer).coerceAtLeast(0)
    }

    private fun taxDays(motorId: Long): Int? {
        val nearest = dbHelper.getPajakByMotor(motorId).firstOrNull { it.status.equals("Belum bayar", true) }
            ?: dbHelper.getPajakByMotor(motorId).firstOrNull()
        return nearest?.let(::daysUntil)
    }

    private fun fuelLiters(motorId: Long): Double {
        return dbHelper.getBensinByMotor(motorId).sumOf { it.liter }
    }

    private fun serviceTargetKm(kilometer: Int, targetOrInterval: Int): Int {
        return if (targetOrInterval > kilometer) targetOrInterval else kilometer + targetOrInterval
    }

    private companion object {
        const val DAY_MILLIS = 86_400_000L
    }
}
