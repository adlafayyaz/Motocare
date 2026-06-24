package com.example.motocare.auth

import android.content.Context

class GoogleAuthManager(context: Context) {
    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isSignedIn(): Boolean = preferences.getBoolean(KEY_SIGNED_IN, false)

    fun signIn() {
        preferences.edit()
            .putBoolean(KEY_SIGNED_IN, true)
            .apply()
    }

    fun signOut() {
        preferences.edit()
            .clear()
            .apply()
    }

    private companion object {
        const val PREF_NAME = "motocare_auth"
        const val KEY_SIGNED_IN = "signed_in"
    }
}
