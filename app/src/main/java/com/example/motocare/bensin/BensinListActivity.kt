package com.example.motocare.bensin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import com.example.motocare.oli.OliListActivity
import com.example.motocare.servis.ServisListActivity

class BensinListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: BensinAdapter
    private lateinit var emptyText: TextView
    private lateinit var summaryValue: TextView
    private lateinit var summaryMeta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bensin_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyBensin)
        summaryValue = findViewById(R.id.textBensinSummaryValue)
        summaryMeta = findViewById(R.id.textBensinSummaryMeta)
        adapter = BensinAdapter { bensin ->
            startActivity(Intent(this, BensinDetailActivity::class.java).putExtra(BensinDetailActivity.EXTRA_BENSIN_ID, bensin.id))
        }

        findViewById<RecyclerView>(R.id.recyclerBensin).apply {
            layoutManager = LinearLayoutManager(this@BensinListActivity)
            adapter = this@BensinListActivity.adapter
        }
        findViewById<Button>(R.id.buttonAddBensin).setOnClickListener {
            startActivity(Intent(this, BensinFormActivity::class.java))
        }
        findViewById<TextView>(R.id.tabRiwayatServis).setOnClickListener {
            startActivity(Intent(this, ServisListActivity::class.java))
        }
        findViewById<TextView>(R.id.tabRiwayatOli).setOnClickListener {
            startActivity(Intent(this, OliListActivity::class.java))
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
            emptyText.visibility = View.VISIBLE
            summaryValue.text = "-"
            summaryMeta.text = getString(R.string.add_motor_first)
            return
        }

        val items = dbHelper.getBensinByMotor(motor.id)
        adapter.submitList(items)
        emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        summaryValue.text = getString(R.string.rupiah_value_compact, items.sumOf { it.cost })
        summaryMeta.text = getString(R.string.fuel_count_value, items.size)
    }
}
