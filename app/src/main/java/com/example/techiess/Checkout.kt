package com.example.techiess

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techiess.SimpleCartAdapter
import com.example.techiess.CartAdapter
import com.example.techiess.CartItem
import com.example.techiess.R
import com.example.techiess.ui.cart.CartFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Checkout : AppCompatActivity() {

    private lateinit var recyclerViewCartConfirmation: RecyclerView
    private lateinit var simpleCartAdapter: SimpleCartAdapter

    private lateinit var tvTotalAmountConfirmation: TextView
    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var btnConfirmCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        recyclerViewCartConfirmation = findViewById(R.id.recyclerViewCartConfirmation)
        recyclerViewCartConfirmation.layoutManager = LinearLayoutManager(this)
        simpleCartAdapter  = SimpleCartAdapter()
        recyclerViewCartConfirmation.adapter = simpleCartAdapter

        tvTotalAmountConfirmation = findViewById(R.id.tvTotalAmountConfirmation)



        // Load user's cart data
        loadUserCartData()

        btnConfirmCheckout = findViewById(R.id.btnConfirmCheckout)
        // Set click listener for the checkout button
        btnConfirmCheckout.setOnClickListener {
                val intent = Intent(this, Information::class.java)
                startActivity(intent)
                finish()

        }

        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            // Create an Intent to go back to the MainActivity
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity
        }
    }


    private fun loadUserCartData() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            val cartReference =
                FirebaseFirestore.getInstance().collection("cart").document(uid)
                    .collection("user_cart")

            cartReference.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    val cartItems = snapshot.toObjects(CartItem::class.java)
                    simpleCartAdapter.setItems(cartItems)

                    // Calculate total amount
                    val totalAmount = calculateTotalAmount(cartItems)
                    tvTotalAmountConfirmation.text = "Total: RM $totalAmount"

                } else {
                    // No data
                    // You can show a message indicating an empty cart
                }
            }
        }
    }

    private fun calculateTotalAmount(cartItems: List<CartItem>): Double {
        var totalAmount = 0.0

        // Iterate through cart items and calculate total amount
        for (cartItem in cartItems) {
            totalAmount += cartItem.productPrice * cartItem.quantity
        }

        return totalAmount
    }
}
