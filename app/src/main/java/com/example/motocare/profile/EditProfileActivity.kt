package com.example.motocare.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R
import com.example.motocare.navigation.BottomNavBinder
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : AppCompatActivity() {
    private lateinit var store: ProfileStore
    private lateinit var nameInput: EditText
    private lateinit var emailText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        store = ProfileStore(this)
        FirebaseAuth.getInstance().currentUser?.let { user ->
            store.saveGoogleProfile(user.displayName, user.email, user.photoUrl?.toString())
        }
        nameInput = findViewById(R.id.editProfileName)
        emailText = findViewById(R.id.textProfileEmail)
        nameInput.setText(store.getName())
        emailText.text = store.getEmail()
        findViewById<TextView>(R.id.textEditProfileInitial).text = store.getName().take(1).uppercase()
        bindAvatar()

        findViewById<Button>(R.id.buttonSaveProfile).setOnClickListener { saveProfile() }
        findViewById<TextView>(R.id.buttonBack).setOnClickListener { finish() }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_PROFILE)
    }

    private fun saveProfile() {
        val name = nameInput.text.toString().trim()
        if (name.isEmpty()) {
            nameInput.error = getString(R.string.error_profile_name_required)
            return
        }
        store.save(name)
        Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun bindAvatar() {
        val image = findViewById<ImageView>(R.id.imageEditProfileAvatar)
        val initial = findViewById<TextView>(R.id.textEditProfileInitial)
        ProfileAvatarLoader.load(image, store.getAvatarUri())
        initial.visibility = View.GONE
    }
}
