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
                applySystemBarInsets(activity)
            }

            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) {
                applySystemBarInsets(activity)
            }
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }

    private fun applySystemBarInsets(activity: Activity) {
        if (activity is DashboardActivity) return
        val root = activity.findViewById<ViewGroup>(android.R.id.content)
            ?.getChildAt(0)
            ?: return
        if (root.getTag(R.id.system_insets_applied_tag) == true) return
        root.setTag(R.id.system_insets_applied_tag, true)
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
