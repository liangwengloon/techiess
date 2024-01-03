package com.example.techiess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Fpx : AppCompatActivity() {
    private lateinit var homeButton: Button
    private lateinit var orderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fpx)



        homeButton = findViewById(R.id.goToHomeButton)
        // Set click listener for the checkout button
        homeButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        orderButton = findViewById(R.id.goToOrderButton)
        // Set click listener for the checkout button
        orderButton.setOnClickListener {
            val intent = Intent(this, Orders::class.java)
            startActivity(intent)
        }
    }
}