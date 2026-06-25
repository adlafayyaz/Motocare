package com.example.motocare.profile

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.navigation.BottomNavBinder

class BackupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)
        findViewById<TextView>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<Button>(R.id.buttonExportData).setOnClickListener {
            Toast.makeText(this, R.string.export_ready_state, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.buttonImportData).setOnClickListener {
            Toast.makeText(this, R.string.import_ready_state, Toast.LENGTH_SHORT).show()
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }
}
