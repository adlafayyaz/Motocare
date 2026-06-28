package com.example.motocare.servis

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper

class ServisDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private var servisId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servis_detail)

        dbHelper = MotoCareDbHelper(this)
        servisId = intent.getLongExtra(EXTRA_SERVIS_ID, 0)

        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textDetailServisType).setOnClickListener { finish() }
        findViewById<Button>(R.id.buttonEditServis).setOnClickListener {
            startActivity(Intent(this, ServisFormActivity::class.java).putExtra(ServisFormActivity.EXTRA_SERVIS_ID, servisId))
        }
        findViewById<Button>(R.id.buttonDeleteServisDetail).setOnClickListener { confirmDelete() }
    }

    override fun onResume() {
        super.onResume()
        bindData()
    }

    private fun bindData() {
        val servis = dbHelper.getServis(servisId) ?: run {
            finish()
            return
        }
        findViewById<TextView>(R.id.textDetailServisType).text = servis.serviceType
        findViewById<TextView>(R.id.textDetailServisDate).text = servis.serviceDate
        findViewById<TextView>(R.id.textDetailServisKm).text = getString(R.string.servis_item_meta, servis.kilometer)
        findViewById<TextView>(R.id.textDetailServisCost).text = getString(R.string.rupiah_value_compact, servis.cost)
        findViewById<TextView>(R.id.textDetailServisInterval).text = getString(
            R.string.target_km_value,
            serviceTargetKm(servis.kilometer, servis.intervalKm)
        )
        findViewById<TextView>(R.id.textDetailServisNote).text = servis.note.ifEmpty { getString(R.string.service_note_empty) }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_servis_title)
            .setMessage(R.string.delete_servis_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteServis(servisId)
                finish()
            }
            .show()
    }

    companion object {
        const val EXTRA_SERVIS_ID = "extra_servis_id"
    }

    private fun serviceTargetKm(kilometer: Int, targetOrInterval: Int): Int {
        return if (targetOrInterval > kilometer) targetOrInterval else kilometer + targetOrInterval
    }
}
