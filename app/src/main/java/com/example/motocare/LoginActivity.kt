package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.auth.GoogleAuthManager
import com.example.motocare.setup.SetupMotorActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var authManager: GoogleAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authManager = GoogleAuthManager(this)
        findViewById<Button>(R.id.buttonGoogleLogin).setOnClickListener {
            authManager.signIn()
            startActivity(Intent(this, SetupMotorActivity::class.java))
            finish()
        }
    }
}
