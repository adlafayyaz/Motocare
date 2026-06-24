package com.example.motocare.oli

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
import com.example.motocare.servis.ServisListActivity

class OliListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: OliAdapter
    private lateinit var emptyText: TextView
    private lateinit var summaryValue: TextView
    private lateinit var summaryMeta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oli_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyOli)
        summaryValue = findViewById(R.id.textOliSummaryValue)
        summaryMeta = findViewById(R.id.textOliSummaryMeta)
        adapter = OliAdapter { oli ->
            val intent = Intent(this, OliDetailActivity::class.java)
            intent.putExtra(OliDetailActivity.EXTRA_OLI_ID, oli.id)
            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.recyclerOli).apply {
            layoutManager = LinearLayoutManager(this@OliListActivity)
            adapter = this@OliListActivity.adapter
        }
        findViewById<Button>(R.id.buttonAddOli).setOnClickListener {
            startActivity(Intent(this, OliFormActivity::class.java))
        }
        findViewById<TextView>(R.id.tabRiwayatServis).setOnClickListener {
            startActivity(Intent(this, ServisListActivity::class.java))
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

        val items = dbHelper.getOliByMotor(motor.id)
        adapter.submitList(items)
        emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE

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
}
