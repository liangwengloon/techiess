package com.example.techiess

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class changePassword : AppCompatActivity() {

    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmNewPasswordEditText: EditText
    private lateinit var resetButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        currentPasswordEditText = findViewById(R.id.currentPassword)
        newPasswordEditText = findViewById(R.id.newPassword)
        confirmNewPasswordEditText = findViewById(R.id.cfmPassword)
        resetButton = findViewById(R.id.resetButton)
        statusTextView = findViewById(R.id.statusTextView)

        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            // Create an Intent to go back to the MainActivity
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity
        }

        resetButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString().trim()
            val newPassword = newPasswordEditText.text.toString().trim()
            val confirmNewPassword = confirmNewPasswordEditText.text.toString().trim()

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                statusTextView.text = "Please fill in all fields."
            } else if (newPassword != confirmNewPassword) {
                statusTextView.text = "New passwords do not match."
            } else {
                // Change password using Firebase Auth
                val user = auth.currentUser
                val credential = auth.currentUser?.email?.let { auth.signInWithEmailAndPassword(it, currentPassword) }

                credential?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user?.updatePassword(newPassword)?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                statusTextView.text = "Password updated successfully."
                                showToast("Password updated successfully.")
                            } else {
                                statusTextView.text = "Failed to update password."
                            }
                        }
                    } else {
                        statusTextView.text = "Authentication failed. Check your current password."
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

