package com.example.techiess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class productDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val title = intent.getStringExtra("title") ?: ""
        val price = intent.getStringExtra("price") ?: ""
        val imageResId = intent.getStringExtra("image") ?: ""
        val os = intent.getStringExtra("OS") ?: ""
        val battery = intent.getStringExtra("battery") ?: ""
        val camera = intent.getStringExtra("camera") ?: ""
        val category = intent.getStringExtra("category") ?: ""
        val desc = intent.getStringExtra("desc") ?: ""
        val display = intent.getStringExtra("display") ?: ""
        val ram = intent.getStringExtra("ram") ?: ""
        val rom = intent.getStringExtra("rom") ?: ""

        // Update UI with product details
        val productTitleTextView: TextView = findViewById(R.id.productTitleDetail)
        val productPriceTextView: TextView = findViewById(R.id.productPriceDetail)
        val productImageImageView: ImageView = findViewById(R.id.productImageDetail)

        productTitleTextView.text = title
        productPriceTextView.text = price
        Glide.with(this)
            .load(imageResId)
            .into(productImageImageView)

        val osDetailTextView: TextView = findViewById(R.id.osDetail)
        osDetailTextView.text = os

    }
}