package com.example.motocare.navigation

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import android.widget.TextView
import com.example.motocare.CatatActivity
import com.example.motocare.DashboardActivity
import com.example.motocare.R
import com.example.motocare.motor.MotorListActivity
import com.example.motocare.profile.ProfileActivity
import com.example.motocare.servis.ServisListActivity

object BottomNavBinder {
    fun bind(activity: Activity, activeMenu: String) {
        activity.findViewById<TextView?>(R.id.navHome)?.apply {
            isSelected = activeMenu == MENU_HOME
            setOnClickListener { open(activity, DashboardActivity::class.java) }
        }
        activity.findViewById<TextView?>(R.id.navMotor)?.apply {
            isSelected = activeMenu == MENU_MOTOR
            setOnClickListener { open(activity, MotorListActivity::class.java) }
        }
        activity.findViewById<TextView?>(R.id.navCatat)?.setOnClickListener {
            open(activity, CatatActivity::class.java)
        }
        activity.findViewById<TextView?>(R.id.navRiwayat)?.setOnClickListener {
            open(activity, ServisListActivity::class.java)
        }
        activity.findViewById<TextView?>(R.id.navProfile)?.setOnClickListener {
            open(activity, ProfileActivity::class.java)
        }
    }

    private fun open(activity: Activity, target: Class<out Activity>) {
        if (activity::class.java == target) return
        activity.startActivity(Intent(activity, target))
    }

    const val MENU_HOME = "home"
    const val MENU_MOTOR = "motor"
    const val MENU_RIWAYAT = "riwayat"
    const val MENU_PROFILE = "profile"
}
