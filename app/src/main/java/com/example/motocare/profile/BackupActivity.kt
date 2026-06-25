package com.example.motocare.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import org.json.JSONObject

class BackupActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private val exportLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri == null) return@registerForActivityResult
        runCatching {
            contentResolver.openOutputStream(uri)?.use { output ->
                output.write(dbHelper.exportJson().toString(2).toByteArray())
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
        setContentView(R.layout.activity_backup)
        dbHelper = MotoCareDbHelper(this)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        findViewById<View>(R.id.rowExportData).setOnClickListener {
            exportLauncher.launch("motocare-backup.json")
        }
        findViewById<View>(R.id.rowImportData).setOnClickListener {
            importLauncher.launch(arrayOf("application/json", "text/*"))
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }
}
