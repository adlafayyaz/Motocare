package com.example.motocare.oli

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper

class OliDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private var oliId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oli_detail)

        dbHelper = MotoCareDbHelper(this)
        oliId = intent.getLongExtra(EXTRA_OLI_ID, 0)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textDetailOliType).setOnClickListener { finish() }

        findViewById<Button>(R.id.buttonEditOli).setOnClickListener {
            startActivity(Intent(this, OliFormActivity::class.java).putExtra(OliFormActivity.EXTRA_OLI_ID, oliId))
        }
        findViewById<Button>(R.id.buttonDeleteOliDetail).setOnClickListener { confirmDelete() }
    }

    override fun onResume() {
        super.onResume()
        bindData()
    }

    private fun bindData() {
        val oli = dbHelper.getOli(oliId) ?: run {
            finish()
            return
        }
        findViewById<TextView>(R.id.textDetailOliType).text = oli.oilType
        findViewById<TextView>(R.id.textDetailOliDate).text = oli.oilChangeDate
        findViewById<TextView>(R.id.textDetailOliKm).text = getString(R.string.oli_item_meta, oli.kilometer)
        findViewById<TextView>(R.id.textDetailOliCost).text = getString(R.string.rupiah_value_compact, oli.cost)
        findViewById<TextView>(R.id.textDetailOliNext).text = getString(R.string.oli_next_km_value, oli.nextKilometer)
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_oli_title)
            .setMessage(R.string.delete_oli_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteOli(oliId)
                finish()
            }
            .show()
    }

    companion object {
        const val EXTRA_OLI_ID = "extra_oli_id"
    }
}
