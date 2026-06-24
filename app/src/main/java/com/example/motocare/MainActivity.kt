package com.example.motocare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.auth.GoogleAuthManager
import com.example.motocare.setup.SetupMotorActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val authManager = GoogleAuthManager(this)
        val nextActivity = if (authManager.isSignedIn()) {
            SetupMotorActivity::class.java
        } else {
            OnboardingActivity::class.java
        }

        window.decorView.postDelayed({
            startActivity(Intent(this, nextActivity))
            finish()
        }, SPLASH_DELAY_MS)
    }

    private companion object {
        const val SPLASH_DELAY_MS = 900L
    }
}
