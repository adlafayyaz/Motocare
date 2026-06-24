package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {
    private val pages = listOf(
        OnboardingPage(
            title = "Pantau semua motor",
            body = "Simpan beberapa motor, pilih motor aktif, lalu lihat KM dan statusnya.",
            indicator = "1 / 3"
        ),
        OnboardingPage(
            title = "Ingat servis dan oli",
            body = "Catat servis dan oli agar target berikutnya lebih mudah dipantau.",
            indicator = "2 / 3"
        ),
        OnboardingPage(
            title = "Catat biaya kendaraan",
            body = "Bensin, servis, oli, dan pajak jadi ringkas dalam satu dashboard.",
            indicator = "3 / 3"
        )
    )

    private var pageIndex = 0
    private lateinit var titleText: TextView
    private lateinit var bodyText: TextView
    private lateinit var indicatorText: TextView
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        titleText = findViewById(R.id.textOnboardingTitle)
        bodyText = findViewById(R.id.textOnboardingBody)
        indicatorText = findViewById(R.id.textOnboardingIndicator)
        nextButton = findViewById(R.id.buttonOnboardingNext)

        findViewById<TextView>(R.id.textSkipIntro).setOnClickListener { openLogin() }
        nextButton.setOnClickListener {
            if (pageIndex == pages.lastIndex) {
                openLogin()
            } else {
                pageIndex += 1
                bindPage()
            }
        }

        bindPage()
    }

    private fun bindPage() {
        val page = pages[pageIndex]
        titleText.text = page.title
        bodyText.text = page.body
        indicatorText.text = page.indicator
        nextButton.text = if (pageIndex == pages.lastIndex) {
            getString(R.string.start)
        } else {
            getString(R.string.next)
        }
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private data class OnboardingPage(
        val title: String,
        val body: String,
        val indicator: String
    )
}
