package com.example.motocare.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.LoginActivity
import com.example.motocare.R
import com.example.motocare.auth.GoogleAuthManager
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {
    private lateinit var store: ProfileStore
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var authManager: GoogleAuthManager
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
            BackupSheet.show(this, exportLauncher, importLauncher)
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
