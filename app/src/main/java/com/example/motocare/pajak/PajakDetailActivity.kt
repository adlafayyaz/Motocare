package com.example.motocare.pajak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper

class PajakDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private var pajakId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pajak_detail)

        dbHelper = MotoCareDbHelper(this)
        pajakId = intent.getLongExtra(EXTRA_PAJAK_ID, 0)
        findViewById<Button>(R.id.buttonEditPajak).setOnClickListener {
            startActivity(Intent(this, PajakFormActivity::class.java).putExtra(PajakFormActivity.EXTRA_PAJAK_ID, pajakId))
        }
        findViewById<Button>(R.id.buttonDeletePajakDetail).setOnClickListener { confirmDelete() }
    }

    override fun onResume() {
        super.onResume()
        bindData()
    }

    private fun bindData() {
        val pajak = dbHelper.getPajak(pajakId) ?: run {
            finish()
            return
        }
        findViewById<TextView>(R.id.textDetailPajakTitle).text = getString(R.string.tax_due_value, pajak.dueDate)
        findViewById<TextView>(R.id.textDetailPajakCost).text = getString(R.string.rupiah_value_compact, pajak.cost)
        findViewById<TextView>(R.id.textDetailPajakStatus).text = pajak.status
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_pajak_title)
            .setMessage(R.string.delete_pajak_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deletePajak(pajakId)
                finish()
            }
            .show()
    }

    companion object {
        const val EXTRA_PAJAK_ID = "extra_pajak_id"
    }
}
