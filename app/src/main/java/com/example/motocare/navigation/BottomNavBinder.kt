package com.example.motocare.navigation

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.motocare.CatatActivity
import com.example.motocare.DashboardActivity
import com.example.motocare.R
import com.example.motocare.motor.MotorListActivity
import com.example.motocare.profile.ProfileActivity
import com.example.motocare.servis.ServisListActivity

object BottomNavBinder {
    fun bind(activity: Activity, activeMenu: String) {
        val bottomNav = activity.findViewById<View?>(R.id.bottomNavRoot)
            ?: activity.findViewById(R.id.dashboardBottomNav)
        bottomNav?.applyBottomInset()
        activity.findViewById<TextView?>(R.id.navHome)?.apply {
            setActiveStyle(activeMenu == MENU_HOME)
            setOnClickListener { open(activity, DashboardActivity::class.java) }
        }
        activity.findViewById<TextView?>(R.id.navMotor)?.apply {
            setActiveStyle(activeMenu == MENU_MOTOR)
            setOnClickListener { open(activity, MotorListActivity::class.java) }
        }
        activity.findViewById<View?>(R.id.navCatat)?.apply {
            setOnClickListener { CatatSheet.show(activity) }
        }
        activity.findViewById<TextView?>(R.id.navRiwayat)?.apply {
            setActiveStyle(activeMenu == MENU_RIWAYAT)
            setOnClickListener { open(activity, ServisListActivity::class.java) }
        }
        activity.findViewById<TextView?>(R.id.navProfile)?.apply {
            setActiveStyle(activeMenu == MENU_PROFILE)
            setOnClickListener { open(activity, ProfileActivity::class.java) }
        }
    }

    private fun open(activity: Activity, target: Class<out Activity>) {
        if (activity::class.java == target) return
        val intent = Intent(activity, target).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        activity.startActivity(intent)
        activity.overridePendingTransition(0, 0)
    }

    private fun TextView.setActiveStyle(active: Boolean) {
        val color = if (active) ACTIVE_COLOR else INACTIVE_COLOR
        setTextColor(color)
        compoundDrawableTintList = ColorStateList.valueOf(color)
    }

    private fun View.applyBottomInset() {
        setBackgroundColor(Color.parseColor("#070A12"))
        if (getTag(R.id.bottom_nav_insets_applied_tag) == true) return
        setTag(R.id.bottom_nav_insets_applied_tag, true)
        val baseHeight = layoutParams.height
        val baseBottom = paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val targetHeight = baseHeight + bottom
            if (view.layoutParams.height != targetHeight) {
                view.layoutParams = view.layoutParams.apply { height = targetHeight }
            }
            view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, baseBottom + bottom)
            insets
        }
        ViewCompat.requestApplyInsets(this)
    }

    const val MENU_HOME = "home"
    const val MENU_MOTOR = "motor"
    const val MENU_CATAT = "catat"
    const val MENU_RIWAYAT = "riwayat"
    const val MENU_PROFILE = "profile"

    private val ACTIVE_COLOR = Color.parseColor("#FFCB25")
    private val INACTIVE_COLOR = Color.parseColor("#8B91A5")
}
