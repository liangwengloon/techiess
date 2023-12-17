package com.example.techiess

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.techiess.ui.cart.CartFragment
import com.example.techiess.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class productDetail : AppCompatActivity() {

    private var quantity = 1
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
        val productID = intent.getStringExtra("productID")


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
        val batteryDetailTextView: TextView = findViewById(R.id.batteryDetail)
        batteryDetailTextView.text = battery
        val cameraDetailTextView: TextView = findViewById(R.id.cameraDetail)
        cameraDetailTextView.text = camera
        val categoryDetailTextView: TextView = findViewById(R.id.categoryDetail)
        categoryDetailTextView.text = category
        val displayDetailTextView: TextView = findViewById(R.id.displayDetail)
        displayDetailTextView.text = display
        val ramDetailTextView: TextView = findViewById(R.id.ramDetail)
        ramDetailTextView.text = ram
        val romDetailTextView: TextView = findViewById(R.id.romDetail)
        romDetailTextView.text = rom
        val descDetailTextView: TextView = findViewById(R.id.descDetail)
        descDetailTextView.text = desc


        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            // Create an Intent to go back to the MainActivity
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity
        }

        val quantityEditText: EditText = findViewById(R.id.quantityEditText)
        val plusButton: Button = findViewById(R.id.plusButton)
        val minusButton: Button = findViewById(R.id.minusButton)
        val addToCartButton: Button = findViewById(R.id.addToCartButton)

        // Increase quantity on plus button click
        plusButton.setOnClickListener {
            quantity++
            quantityEditText.setText(quantity.toString())
        }

        // Decrease quantity on minus button click
        minusButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityEditText.setText(quantity.toString())
            }
        }

        // Add to Cart button click
        addToCartButton.setOnClickListener {
            val cartItem = hashMapOf(
                "productID" to productID,
                "productImage" to imageResId,
                "productName" to title,
                "productPrice" to price,
                "quantity" to quantity.toString()
            )

            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid

            // Get the Firestore instance
            val db = FirebaseFirestore.getInstance()

            // Check if the product already exists in the user's cart
            if (uid != null) {
                val userCartRef = db.collection("cart").document(uid).collection("user_cart")
                userCartRef.whereEqualTo("productID", productID)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) {
                            // Product not found in cart, add a new document
                            userCartRef.add(cartItem)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(this, "Added to Cart.", Toast.LENGTH_LONG).show()
                                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to Add Cart", Toast.LENGTH_LONG).show()
                                    Log.w(TAG, "Error adding document", e)
                                }
                        } else {
                            // Product found in cart, update the existing document with new quantity
                            val existingDocumentId = result.documents[0].id
                            val existingQuantity = result.documents[0].getString("quantity")?.toInt() ?: 0
                            val newQuantity = existingQuantity + quantity
                            userCartRef.document(existingDocumentId)
                                .update("quantity", newQuantity.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Cart updated.", Toast.LENGTH_LONG).show()
                                    Log.d(TAG, "DocumentSnapshot updated with ID: $existingDocumentId")
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to update Cart", Toast.LENGTH_LONG).show()
                                    Log.w(TAG, "Error updating document", e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error checking Cart", Toast.LENGTH_LONG).show()
                        Log.w(TAG, "Error checking Cart", e)
                    }
            }
        }


    }
}