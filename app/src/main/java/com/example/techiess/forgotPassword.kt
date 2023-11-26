package com.example.techiess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.techiess.databinding.ActivityForgotPasswordBinding  // Import the generated binding class

class forgotPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding   // Declare a variable for the binding class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)  // Initialize the binding class
        setContentView(binding.root)  // Use the binding class root as the content view

        auth = FirebaseAuth.getInstance()


        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            // Create an Intent to go back to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity
        }

        binding.resetButton.setOnClickListener {  // Use binding class to access views
            val email = binding.emailEditText.text.toString().trim()  // Use binding class to access views

            if (email.isEmpty()) {
                binding.emailEditText.error = "Email required"
                binding.emailEditText.requestFocus()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Check your email for OTP", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error occurred: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
