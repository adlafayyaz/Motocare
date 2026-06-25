package com.example.motocare.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.navigation.BottomNavBinder

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        findViewById<View>(R.id.rowBackup).setOnClickListener {
            startActivity(Intent(this, BackupActivity::class.java))
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }
}
