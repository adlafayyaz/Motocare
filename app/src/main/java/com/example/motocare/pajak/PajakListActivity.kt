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
        summaryValue.text = nearest?.dueDate ?: getString(R.string.no_data_short)
        summaryMeta.text = nearest?.let { getString(R.string.rupiah_value_compact, it.cost) } ?: getString(R.string.empty_pajak)
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
}
