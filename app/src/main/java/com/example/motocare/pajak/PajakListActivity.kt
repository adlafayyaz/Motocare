package com.example.motocare.pajak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.bensin.BensinListActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import com.example.motocare.oli.OliListActivity
import com.example.motocare.servis.ServisListActivity

class PajakListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: PajakAdapter
    private lateinit var emptyText: TextView
    private lateinit var summaryValue: TextView
    private lateinit var summaryMeta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pajak_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyPajak)
        summaryValue = findViewById(R.id.textPajakSummaryValue)
        summaryMeta = findViewById(R.id.textPajakSummaryMeta)
        adapter = PajakAdapter { pajak ->
            startActivity(Intent(this, PajakDetailActivity::class.java).putExtra(PajakDetailActivity.EXTRA_PAJAK_ID, pajak.id))
        }

        findViewById<RecyclerView>(R.id.recyclerPajak).apply {
            layoutManager = LinearLayoutManager(this@PajakListActivity)
            adapter = this@PajakListActivity.adapter
        }
        findViewById<Button>(R.id.buttonAddPajak).setOnClickListener {
            startActivity(Intent(this, PajakFormActivity::class.java))
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
            emptyText.visibility = View.VISIBLE
            summaryValue.text = "-"
            summaryMeta.text = getString(R.string.add_motor_first)
            return
        }

        val items = dbHelper.getPajakByMotor(motor.id)
        adapter.submitList(items)
        emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        val nearest = items.firstOrNull()
        summaryValue.text = nearest?.dueDate ?: getString(R.string.no_data_short)
        summaryMeta.text = nearest?.let { getString(R.string.rupiah_value_compact, it.cost) } ?: getString(R.string.empty_pajak)
    }
}
