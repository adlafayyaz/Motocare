package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView

class OnboardingActivity : AppCompatActivity() {
    private val pages = listOf(
        OnboardingPage(
            title = "Pantau semua motor",
            body = "Simpan beberapa motor, pilih motor aktif, lalu lihat KM dan statusnya.",
            imageRes = R.drawable.onboarding1_img
        ),
        OnboardingPage(
            title = "Ingat servis dan oli",
            body = "MotoCare hitung target servis, sisa jarak oli, dan jadwal berikutnya.",
            imageRes = R.drawable.onboarding2_img
        ),
        OnboardingPage(
            title = "Catat biaya kendaraan",
            body = "Bensin, servis, oli, dan pajak jadi ringkas dalam satu dashboard.",
            imageRes = R.drawable.onboarding3_img
        )
    )

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsContainer: LinearLayout
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPagerOnboarding)
        dotsContainer = findViewById(R.id.layoutOnboardingDots)
        nextButton = findViewById(R.id.buttonOnboardingNext)
        viewPager.adapter = OnboardingAdapter(pages)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bindPage(position)
            }
        })

        findViewById<TextView>(R.id.textSkipIntro).setOnClickListener { openLogin() }
        nextButton.setOnClickListener {
            val current = viewPager.currentItem
            if (current == pages.lastIndex) {
                openLogin()
            } else {
                viewPager.currentItem = current + 1
            }
        }

        bindPage(0)
    }

    private fun bindPage(position: Int) {
        nextButton.text = if (position == pages.lastIndex) {
            getString(R.string.start)
        } else {
            getString(R.string.next)
        }
        bindDots(position)
    }

    private fun bindDots(activePosition: Int) {
        dotsContainer.removeAllViews()
        pages.forEachIndexed { index, _ ->
            val dot = View(this)
            val width = if (index == activePosition) dp(80) else dp(34)
            val params = LinearLayout.LayoutParams(width, dp(34)).apply {
                marginStart = dp(10)
                marginEnd = dp(10)
            }
            dot.background = ContextCompat.getDrawable(
                this,
                if (index == activePosition) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive
            )
            dotsContainer.addView(dot, params)
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private data class OnboardingPage(
        val title: String,
        val body: String,
        val imageRes: Int
    )

    private class OnboardingAdapter(
        private val pages: List<OnboardingPage>
    ) : RecyclerView.Adapter<OnboardingAdapter.PageViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_onboarding_page, parent, false)
            return PageViewHolder(view)
        }

        override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
            holder.bind(pages[position])
        }

        override fun getItemCount(): Int = pages.size

        class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val image: ImageView = itemView.findViewById(R.id.imageOnboarding)
            private val title: TextView = itemView.findViewById(R.id.textOnboardingTitle)
            private val body: TextView = itemView.findViewById(R.id.textOnboardingBody)

            fun bind(page: OnboardingPage) {
                image.setImageResource(page.imageRes)
                title.text = page.title
                body.text = page.body
            }
        }
    }
}
