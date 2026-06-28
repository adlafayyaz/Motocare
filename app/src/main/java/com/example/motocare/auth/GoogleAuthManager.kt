package com.example.motocare.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.motocare.R
import com.example.motocare.profile.ProfileStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthManager(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()

    fun isSignedIn(): Boolean = auth.currentUser != null

    fun getSignInIntent(): Intent {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(context, options).signInIntent
    }

    fun handleSignInResult(
        data: Intent?,
        activity: Activity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = runCatching { accountTask.getResult(ApiException::class.java) }.getOrElse {
            onError(mapGoogleError(it))
            return
        }
        val idToken = account.idToken
        if (idToken == null) {
            onError("Google ID token missing")
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    ProfileStore(context).saveGoogleProfile(
                        name = user?.displayName ?: account.displayName,
                        email = user?.email ?: account.email,
                        avatarUri = user?.photoUrl?.toString() ?: account.photoUrl?.toString()
                    )
                    onSuccess()
                } else {
                    onError(task.exception?.localizedMessage ?: "Firebase sign-in failed")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut()
    }

    private fun mapGoogleError(error: Throwable): String {
        if (error !is ApiException) return error.localizedMessage ?: "Google login gagal."
        return when (error.statusCode) {
            GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Google login dibatalkan."
            GoogleSignInStatusCodes.SIGN_IN_FAILED -> "Google login gagal. Cek akun Google dan Firebase."
            GoogleSignInStatusCodes.NETWORK_ERROR -> "Google login gagal: koneksi internet atau Google Play Services bermasalah."
            GoogleSignInStatusCodes.DEVELOPER_ERROR -> "Google login gagal: SHA-1/debug key belum cocok di Firebase."
            else -> "Google login gagal (${error.statusCode})."
        }
    }
}
