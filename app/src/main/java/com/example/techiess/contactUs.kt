package com.example.techiess

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class contactUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            // Create an Intent to go back to the MainActivity
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity
        }
    }

    fun callPhoneNumber(view: View) {
        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumber)
        val phoneNumber = phoneNumberTextView.text.toString()
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    // Handle "Open Map" click
    fun openMap(view: View) {
        // Implement the logic to open Google Maps or any map application
        // You can use the location information to create a map intent
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:3.01648426545597,101.61534145767067?q=29 JALAN PUTERI 5/5, BANDAR PUTERI PUCHONG, 47100 SELANGOR, MALAYSIA")
        )

        startActivity(intent)
    }
}