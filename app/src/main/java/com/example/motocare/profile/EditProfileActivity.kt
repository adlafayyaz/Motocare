package com.example.motocare.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.R

class EditProfileActivity : AppCompatActivity() {
    private lateinit var store: ProfileStore
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        store = ProfileStore(this)
        nameInput = findViewById(R.id.editProfileName)
        emailInput = findViewById(R.id.editProfileEmail)
        nameInput.setText(store.getName())
        emailInput.setText(store.getEmail())

        findViewById<Button>(R.id.buttonSaveProfile).setOnClickListener { saveProfile() }
    }

    private fun saveProfile() {
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        if (name.isEmpty()) {
            nameInput.error = getString(R.string.error_profile_name_required)
            return
        }
        if (email.isEmpty()) {
            emailInput.error = getString(R.string.error_profile_email_required)
            return
        }
        store.save(name, email)
        Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()
        finish()
    }
}
