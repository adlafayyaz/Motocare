package com.example.motocare.profile

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class ProfileStore(context: Context) {
    private val prefs = context.getSharedPreferences("profile_${accountKey()}", Context.MODE_PRIVATE)

    fun getName(): String = prefs.getString(KEY_NAME, "Adla") ?: "Adla"

    fun getEmail(): String = prefs.getString(KEY_EMAIL, "google.user@example.com") ?: "google.user@example.com"

    fun getAvatarUri(): String? = prefs.getString(KEY_AVATAR_URI, null)

    fun save(name: String) {
        prefs.edit()
            .putString(KEY_NAME, name)
            .putBoolean(KEY_CUSTOM_NAME, true)
            .apply()
    }

    fun saveGoogleProfile(name: String?, email: String?, avatarUri: String?) {
        val hasCustomName = prefs.getBoolean(KEY_CUSTOM_NAME, false)
        prefs.edit()
            .apply {
                if (!hasCustomName) {
                    putString(KEY_NAME, name?.takeIf { it.isNotBlank() } ?: getName())
                }
            }
            .putString(KEY_EMAIL, email?.takeIf { it.isNotBlank() } ?: getEmail())
            .putString(KEY_AVATAR_URI, avatarUri)
            .apply()
    }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_AVATAR_URI = "avatar_uri"
        private const val KEY_CUSTOM_NAME = "custom_name"

        private fun accountKey(): String {
            val user = FirebaseAuth.getInstance().currentUser
            val raw = user?.uid ?: user?.email ?: "local"
            return raw.replace(Regex("[^A-Za-z0-9_]"), "_")
        }
    }
}
