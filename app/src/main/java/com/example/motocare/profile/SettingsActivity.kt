package com.example.motocare.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import org.json.JSONObject

class SettingsActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private val exportLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri == null) return@registerForActivityResult
        runCatching {
            contentResolver.openOutputStream(uri)?.use {
                it.write(dbHelper.exportJson().toString(2).toByteArray())
            }
        }.onSuccess {
            Toast.makeText(this, R.string.export_done, Toast.LENGTH_SHORT).show()
        }.onFailure {
            Toast.makeText(this, R.string.export_failed, Toast.LENGTH_SHORT).show()
        }
    }
    private val importLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@registerForActivityResult
        runCatching {
            val text = contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }.orEmpty()
            dbHelper.importJson(JSONObject(text))
        }.onSuccess {
            Toast.makeText(this, R.string.import_done, Toast.LENGTH_SHORT).show()
        }.onFailure {
            Toast.makeText(this, R.string.import_failed, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        dbHelper = MotoCareDbHelper(this)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        findViewById<View>(R.id.rowBackup).setOnClickListener {
            BackupSheet.show(this, exportLauncher, importLauncher)
        }
        findViewById<View>(R.id.rowResetData).setOnClickListener { confirmReset() }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }

    private fun confirmReset() {
        AlertDialog.Builder(this)
            .setTitle(R.string.reset_confirm_title)
            .setMessage(R.string.reset_confirm_body)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.reset_data) { _, _ ->
                dbHelper.resetAllData()
                Toast.makeText(this, R.string.reset_done, Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
