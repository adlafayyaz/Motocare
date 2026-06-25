package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.auth.GoogleAuthManager
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.setup.SetupMotorActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var authManager: GoogleAuthManager
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            authManager.handleSignInResult(
                data = result.data,
                activity = this,
                onSuccess = {
                    val next = if (MotoCareDbHelper(this).getAllMotors().isEmpty()) {
                        SetupMotorActivity::class.java
                    } else {
                        DashboardActivity::class.java
                    }
                    startActivity(Intent(this, next))
                    finish()
                },
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authManager = GoogleAuthManager(this)
        findViewById<Button>(R.id.buttonGoogleLogin).setOnClickListener {
            googleSignInLauncher.launch(authManager.getSignInIntent())
        }
    }
}
