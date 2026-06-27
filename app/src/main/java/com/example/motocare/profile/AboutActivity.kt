package com.example.motocare.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.navigation.BottomNavBinder

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        findViewById<View>(R.id.buttonBack).setOnClickListener { finish() }
        findViewById<View>(R.id.textTitleBack).setOnClickListener { finish() }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }
}
