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
        val imageResId = intent.getIntExtra("image", R.drawable.logo)

        // Update UI with product details
        val productTitleTextView: TextView = findViewById(R.id.productTitleDetail)
        val productPriceTextView: TextView = findViewById(R.id.productPriceDetail)
        val productImageImageView: ImageView = findViewById(R.id.productImageDetail)

        productTitleTextView.text = title
        productPriceTextView.text = price

        Glide.with(this)
            .load(imageResId)
            .into(productImageImageView)
    }
}