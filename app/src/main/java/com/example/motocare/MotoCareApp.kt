package com.example.motocare

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat

class MotoCareApp : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                WindowCompat.setDecorFitsSystemWindows(activity.window, false)
                if (activity !is DashboardActivity) {
                    applySystemBarInsets(activity)
                }
            }

            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }

    private fun applySystemBarInsets(activity: Activity) {
        val root = activity.findViewById<ViewGroup>(android.R.id.content)
            ?.getChildAt(0)
            ?: return
        val baseLeft = root.paddingLeft
        val baseTop = root.paddingTop
        val baseRight = root.paddingRight
        val baseBottom = root.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val hasBottomNav = activity.findViewById<View?>(R.id.bottomNavRoot) != null
            view.setPadding(
                baseLeft,
                baseTop + bars.top,
                baseRight,
                baseBottom + if (hasBottomNav) 0 else bars.bottom
            )
            insets
        }
        ViewCompat.requestApplyInsets(root)
    }
}
