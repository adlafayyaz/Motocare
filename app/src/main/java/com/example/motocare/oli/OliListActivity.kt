package com.example.motocare.oli

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
import com.example.motocare.pajak.PajakListActivity
import com.example.motocare.servis.ServisListActivity

class OliListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: OliAdapter
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
        setContentView(R.layout.activity_oli_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyOli)
        summaryValue = findViewById(R.id.textOliSummaryValue)
        summaryMeta = findViewById(R.id.textOliSummaryMeta)
        motorName = findViewById(R.id.textHistoryMotorName)
        motorPlate = findViewById(R.id.textHistoryMotorPlate)
        motorCard = findViewById(R.id.historyMotorCard)
        summaryCard = findViewById(R.id.historySummaryCard)
        emptyState = findViewById(R.id.historyEmptyState)
        recycler = findViewById(R.id.recyclerOli)
        adapter = OliAdapter { oli ->
            val intent = Intent(this, OliDetailActivity::class.java)
            intent.putExtra(OliDetailActivity.EXTRA_OLI_ID, oli.id)
            startActivity(intent)
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(this@OliListActivity)
            adapter = this@OliListActivity.adapter
        }
        findViewById<TextView>(R.id.buttonChangeMotor).setOnClickListener {
            CatatSheet.showMotorPicker(this) { bindData() }
        }
        findViewById<TextView>(R.id.tabRiwayatServis).setOnClickListener {
            startActivity(Intent(this, ServisListActivity::class.java))
        }
        findViewById<TextView>(R.id.tabRiwayatBensin).setOnClickListener {
            startActivity(Intent(this, BensinListActivity::class.java))
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
        val items = dbHelper.getOliByMotor(motor.id)
        adapter.submitList(items)
        bindEmptyVisibility(items.isEmpty())

        val latest = dbHelper.getLatestOli(motor.id)
        if (latest == null) {
            summaryValue.text = getString(R.string.no_data_short)
            summaryMeta.text = getString(R.string.no_oli_data_short)
        } else {
            val remainingKm = (latest.nextKilometer - motor.currentKilometer).coerceAtLeast(0)
            summaryValue.text = getString(R.string.km_remaining_value, remainingKm)
            summaryMeta.text = getString(R.string.oli_next_km_value, latest.nextKilometer)
        }
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
