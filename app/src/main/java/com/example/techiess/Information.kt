package com.example.techiess

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.techiess.R
import com.example.techiess.databinding.ActivityInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase



class Information : AppCompatActivity() {

    data class CartItem2(
        val productID: String = "",
        val productName: String = "",
        val productPrice: Double = 0.0,
        var quantity: Int = 0,
        val productImage: String = ""
    )
    private lateinit var binding: ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the payment methods array from resources
        val paymentMethods = resources.getStringArray(R.array.payment_methods)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.spinnerPaymentMethod.adapter = adapter

        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        val continueButton = findViewById<Button>(R.id.btnContinue)
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("cart")
        val ordersRef = database.getReference("orders")

        val user = FirebaseAuth.getInstance().currentUser
        val userId: String = user?.uid ?: ""
        Log.d(ContentValues.TAG, "User ID: $userId")

        // Add the user ID under the "orders" node
        val userOrderRef = ordersRef.child(userId).push()

        val userCartRef = cartRef.child(userId).child("user_cart")

        continueButton.setOnClickListener {
            Log.d(ContentValues.TAG, "Before attaching ChildEventListener")
            userCartRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(ContentValues.TAG, "onChildAdded triggered")

                    // Get product data
                    val productData = snapshot.getValue(CartItem2::class.java)
                    Log.d(ContentValues.TAG, "User ID: $productData")

                    if (productData != null) {
                        // Copy the product to the order without creating a new auto-generated ID
                        userOrderRef.child(snapshot.key ?: "").setValue(productData)
                    } else {
                        Log.e(ContentValues.TAG, "Product Data is null")
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // Handle changes if needed
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // Handle removal if needed
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // Handle movement if needed
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(ContentValues.TAG, "Firebase Error22: ${error.message}")
                }

            })
            Log.d(ContentValues.TAG, "After attaching ChildEventListener")

        }
    }
}
