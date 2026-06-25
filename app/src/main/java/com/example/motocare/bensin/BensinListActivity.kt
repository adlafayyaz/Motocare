package com.example.motocare.bensin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import com.example.motocare.navigation.CatatSheet
import com.example.motocare.oli.OliListActivity
import com.example.motocare.pajak.PajakListActivity
import com.example.motocare.servis.ServisListActivity

class BensinListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: BensinAdapter
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
        setContentView(R.layout.activity_bensin_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyBensin)
        summaryValue = findViewById(R.id.textBensinSummaryValue)
        summaryMeta = findViewById(R.id.textBensinSummaryMeta)
        motorName = findViewById(R.id.textHistoryMotorName)
        motorPlate = findViewById(R.id.textHistoryMotorPlate)
        motorCard = findViewById(R.id.historyMotorCard)
        summaryCard = findViewById(R.id.historySummaryCard)
        emptyState = findViewById(R.id.historyEmptyState)
        recycler = findViewById(R.id.recyclerBensin)
        adapter = BensinAdapter { bensin ->
            startActivity(Intent(this, BensinDetailActivity::class.java).putExtra(BensinDetailActivity.EXTRA_BENSIN_ID, bensin.id))
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(this@BensinListActivity)
            adapter = this@BensinListActivity.adapter
        }
        findViewById<TextView>(R.id.buttonChangeMotor).setOnClickListener {
            CatatSheet.showMotorPicker(this) { bindData() }
        }
        findViewById<TextView>(R.id.tabRiwayatServis).setOnClickListener {
            startActivity(Intent(this, ServisListActivity::class.java))
        }
        findViewById<TextView>(R.id.tabRiwayatOli).setOnClickListener {
            startActivity(Intent(this, OliListActivity::class.java))
        }
        findViewById<TextView>(R.id.tabRiwayatPajak).setOnClickListener {
            startActivity(Intent(this, PajakListActivity::class.java))
        }
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
        val items = dbHelper.getBensinByMotor(motor.id)
        adapter.submitList(items)
        bindEmptyVisibility(items.isEmpty())
        summaryValue.text = getString(R.string.rupiah_value_compact, items.sumOf { it.cost })
        summaryMeta.text = getString(R.string.fuel_count_value, items.size)
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
