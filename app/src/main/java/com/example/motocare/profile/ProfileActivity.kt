package com.example.motocare.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.LoginActivity
import com.example.motocare.R
import com.example.motocare.auth.GoogleAuthManager
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var store: ProfileStore
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var authManager: GoogleAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        store = ProfileStore(this)
        dbHelper = MotoCareDbHelper(this)
        authManager = GoogleAuthManager(this)

        findViewById<View>(R.id.rowEditProfile).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        findViewById<View>(R.id.rowSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<View>(R.id.rowBackup).setOnClickListener {
            startActivity(Intent(this, BackupActivity::class.java))
        }
        findViewById<View>(R.id.rowAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        findViewById<View>(R.id.rowLogout).setOnClickListener {
            authManager.signOut()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }

    override fun onResume() {
        super.onResume()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            store.saveGoogleProfile(user.displayName, user.email, user.photoUrl?.toString())
        }
        findViewById<TextView>(R.id.textProfileInitial).text = store.getName().take(1).uppercase()
        findViewById<TextView>(R.id.textProfileName).text = store.getName()
        findViewById<TextView>(R.id.textProfileEmail).text =
            getString(R.string.profile_motor_count, dbHelper.getAllMotors().size)
        bindAvatar()
    }

    private fun bindAvatar() {
        val image = findViewById<ImageView>(R.id.imageProfileAvatar)
        val initial = findViewById<TextView>(R.id.textProfileInitial)
        ProfileAvatarLoader.load(image, store.getAvatarUri())
        initial.visibility = View.GONE
    }
}
