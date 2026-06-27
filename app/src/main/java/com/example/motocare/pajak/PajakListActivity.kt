package com.example.motocare.pajak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.bensin.BensinListActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import com.example.motocare.navigation.CatatSheet
import com.example.motocare.oli.OliListActivity
import com.example.motocare.servis.ServisListActivity
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.ceil

class PajakListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: PajakAdapter
    private lateinit var emptyText: TextView
    private lateinit var summaryValue: TextView
    private lateinit var summaryMeta: TextView
    private lateinit var motorName: TextView
    private lateinit var motorPlate: TextView
    private lateinit var motorCard: View
    private lateinit var summaryCard: View
    private lateinit var emptyState: View
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pajak_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyPajak)
        summaryValue = findViewById(R.id.textPajakSummaryValue)
        summaryMeta = findViewById(R.id.textPajakSummaryMeta)
        motorName = findViewById(R.id.textHistoryMotorName)
        motorPlate = findViewById(R.id.textHistoryMotorPlate)
        motorCard = findViewById(R.id.historyMotorCard)
        summaryCard = findViewById(R.id.historySummaryCard)
        emptyState = findViewById(R.id.historyEmptyState)
        recycler = findViewById(R.id.recyclerPajak)
        adapter = PajakAdapter { pajak ->
            startActivity(Intent(this, PajakDetailActivity::class.java).putExtra(PajakDetailActivity.EXTRA_PAJAK_ID, pajak.id))
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(this@PajakListActivity)
            adapter = this@PajakListActivity.adapter
        }
        findViewById<TextView>(R.id.buttonChangeMotor).setOnClickListener {
            CatatSheet.showMotorPicker(this) { bindData() }
        }
        findViewById<TextView>(R.id.tabRiwayatServis).setOnClickListener { startActivity(Intent(this, ServisListActivity::class.java)) }
        findViewById<TextView>(R.id.tabRiwayatOli).setOnClickListener { startActivity(Intent(this, OliListActivity::class.java)) }
        findViewById<TextView>(R.id.tabRiwayatBensin).setOnClickListener { startActivity(Intent(this, BensinListActivity::class.java)) }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_RIWAYAT)
    }

    override fun onResume() {
        super.onResume()
        bindData()
    }

    private fun bindData() {
        val motor = dbHelper.getActiveMotor()
        if (motor == null) {
            adapter.submitList(emptyList())
            showEmptyState()
            summaryValue.text = "-"
            summaryMeta.text = getString(R.string.add_motor_first)
            setSummaryStatusColor(false)
            motorName.text = getString(R.string.no_active_motor)
            motorPlate.text = ""
            return
        }

        motorName.text = motor.name
        motorPlate.text = motor.plateNumber
        val items = dbHelper.getPajakByMotor(motor.id)
        adapter.submitList(items)
        bindEmptyVisibility(items.isEmpty())
        val nearest = items.firstOrNull()
        val overdue = nearest?.let {
            it.status.equals("Belum bayar", true) && (daysUntil(it.dueDate) ?: 1) <= 0
        } == true
        summaryValue.text = when {
            nearest == null -> getString(R.string.no_data_short)
            overdue -> getString(R.string.tax_due_now)
            else -> nearest.dueDate
        }
        summaryMeta.text = nearest?.let { getString(R.string.rupiah_value_compact, it.cost) } ?: getString(R.string.empty_pajak)
        setSummaryStatusColor(overdue)
    }

    private fun setSummaryStatusColor(overdue: Boolean) {
        val color = getColor(if (overdue) R.color.motocare_error else R.color.motocare_text)
        val metaColor = getColor(if (overdue) R.color.motocare_error else R.color.motocare_yellow)
        summaryValue.setTextColor(color)
        summaryMeta.setTextColor(metaColor)
    }

    private fun daysUntil(date: String): Int? {
        return runCatching {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val due = format.parse(date) ?: return null
            val today = format.parse(format.format(System.currentTimeMillis())) ?: return null
            ceil((due.time - today.time) / DAY_MILLIS.toDouble()).toInt()
        }.getOrNull()
    }

    private fun bindEmptyVisibility(isEmpty: Boolean) {
        emptyText.visibility = View.GONE
        emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        motorCard.visibility = View.VISIBLE
        summaryCard.visibility = if (isEmpty) View.GONE else View.VISIBLE
        recycler.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showEmptyState() {
        bindEmptyVisibility(true)
    }

    private companion object {
        const val DAY_MILLIS = 86_400_000L
    }
}
