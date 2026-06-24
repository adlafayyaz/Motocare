package com.example.motocare.profile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.navigation.BottomNavBinder

class ProfileActivity : AppCompatActivity() {
    private lateinit var store: ProfileStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        store = ProfileStore(this)

        findViewById<TextView>(R.id.rowEditProfile).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        findViewById<TextView>(R.id.rowSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<TextView>(R.id.rowBackup).setOnClickListener {
            startActivity(Intent(this, BackupActivity::class.java))
        }
        findViewById<TextView>(R.id.rowAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.textProfileInitial).text = store.getName().take(1).uppercase()
        findViewById<TextView>(R.id.textProfileName).text = store.getName()
        findViewById<TextView>(R.id.textProfileEmail).text = store.getEmail()
    }
}
