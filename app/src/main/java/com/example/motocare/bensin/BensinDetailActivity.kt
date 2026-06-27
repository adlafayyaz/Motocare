package com.example.motocare.bensin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper

class BensinDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private var bensinId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bensin_detail)

        dbHelper = MotoCareDbHelper(this)
        bensinId = intent.getLongExtra(EXTRA_BENSIN_ID, 0)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textDetailBensinTitle).setOnClickListener { finish() }
        findViewById<Button>(R.id.buttonEditBensin).setOnClickListener {
            startActivity(Intent(this, BensinFormActivity::class.java).putExtra(BensinFormActivity.EXTRA_BENSIN_ID, bensinId))
        }
        findViewById<Button>(R.id.buttonDeleteBensinDetail).setOnClickListener { confirmDelete() }
    }

    override fun onResume() {
        super.onResume()
        bindData()
    }

    private fun bindData() {
        val bensin = dbHelper.getBensin(bensinId) ?: run {
            finish()
            return
        }
        findViewById<TextView>(R.id.textDetailBensinTitle).text = getString(R.string.fuel_title_value, bensin.fuelBrand, bensin.octane)
        findViewById<TextView>(R.id.textDetailBensinCost).text = getString(R.string.rupiah_value_compact, bensin.cost)
        findViewById<TextView>(R.id.textDetailBensinDate).text = bensin.fuelDate
        findViewById<TextView>(R.id.textDetailBensinLiter).text = getString(R.string.fuel_liter_value, bensin.liter)
        findViewById<TextView>(R.id.textDetailBensinPrice).text = getString(R.string.fuel_price_value, bensin.pricePerLiter)
        findViewById<TextView>(R.id.textDetailBensinKm).text = getString(R.string.servis_item_meta, bensin.kilometer)
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_bensin_title)
            .setMessage(R.string.delete_bensin_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteBensin(bensinId)
                finish()
            }
            .show()
    }

    companion object {
        const val EXTRA_BENSIN_ID = "extra_bensin_id"
    }
}
