package com.example.motocare.profile

import android.content.Context

class ProfileStore(context: Context) {
    private val prefs = context.getSharedPreferences("profile", Context.MODE_PRIVATE)

    fun getName(): String = prefs.getString(KEY_NAME, "Adla") ?: "Adla"

    fun getEmail(): String = prefs.getString(KEY_EMAIL, "google.user@example.com") ?: "google.user@example.com"

    fun save(name: String, email: String) {
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }
}
