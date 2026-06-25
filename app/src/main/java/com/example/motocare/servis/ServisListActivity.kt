package com.example.motocare.servis

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
import com.example.motocare.pajak.PajakListActivity

class ServisListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: ServisAdapter
    private lateinit var emptyText: TextView
    private lateinit var summaryTitle: TextView
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
        setContentView(R.layout.activity_servis_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyServis)
        summaryTitle = findViewById(R.id.textServisSummaryTitle)
        summaryValue = findViewById(R.id.textServisSummaryValue)
        summaryMeta = findViewById(R.id.textServisSummaryMeta)
        motorName = findViewById(R.id.textHistoryMotorName)
        motorPlate = findViewById(R.id.textHistoryMotorPlate)
        motorCard = findViewById(R.id.historyMotorCard)
        summaryCard = findViewById(R.id.historySummaryCard)
        emptyState = findViewById(R.id.historyEmptyState)
        recycler = findViewById(R.id.recyclerServis)
        adapter = ServisAdapter { servis ->
            val intent = Intent(this, ServisDetailActivity::class.java)
            intent.putExtra(ServisDetailActivity.EXTRA_SERVIS_ID, servis.id)
            startActivity(intent)
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(this@ServisListActivity)
            adapter = this@ServisListActivity.adapter
        }
        findViewById<TextView>(R.id.buttonChangeMotor).setOnClickListener {
            CatatSheet.showMotorPicker(this) { bindData() }
        }
        findViewById<TextView>(R.id.tabRiwayatOli).setOnClickListener {
            startActivity(Intent(this, OliListActivity::class.java))
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
            summaryTitle.text = getString(R.string.no_active_motor)
            summaryValue.text = "-"
            summaryMeta.text = getString(R.string.add_motor_first)
            motorName.text = getString(R.string.no_active_motor)
            motorPlate.text = ""
            return
        }

        motorName.text = motor.name
        motorPlate.text = motor.plateNumber
        val items = dbHelper.getServisByMotor(motor.id)
        adapter.submitList(items)
        emptyText.visibility = View.GONE
        val isEmpty = items.isEmpty()
        emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        motorCard.visibility = View.VISIBLE
        summaryCard.visibility = if (isEmpty) View.GONE else View.VISIBLE
        recycler.visibility = if (isEmpty) View.GONE else View.VISIBLE

        val latest = dbHelper.getLatestServis(motor.id)
        if (latest == null) {
            summaryTitle.text = getString(R.string.next_service_title)
            summaryValue.text = getString(R.string.no_data_short)
            summaryMeta.text = getString(R.string.no_service_data_short)
        } else {
            val targetKm = latest.kilometer + latest.intervalKm
            val remainingKm = (targetKm - motor.currentKilometer).coerceAtLeast(0)
            summaryTitle.text = getString(R.string.next_service_title)
            summaryValue.text = getString(R.string.km_remaining_value, remainingKm)
            summaryMeta.text = getString(R.string.target_km_value, targetKm)
        }
    }

    private fun showEmptyState() {
        emptyText.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
        motorCard.visibility = View.VISIBLE
        summaryCard.visibility = View.GONE
        recycler.visibility = View.GONE
    }
}
