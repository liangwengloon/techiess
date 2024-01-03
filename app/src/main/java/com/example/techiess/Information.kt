package com.example.techiess


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.techiess.R
import com.example.techiess.CartAdapter
import com.example.techiess.databinding.ActivityInformationBinding
import com.example.techiess.ui.home.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Information : AppCompatActivity() {

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
            val intent = Intent(this, Checkout::class.java)
            startActivity(intent)
            finish()
        }

        val continueButton =findViewById<Button>(R.id.btnContinue)
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("cart")
        val ordersRef = database.getReference("orders")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        data class CartItem2(
            val productID: String = "",
            val productName: String = "",
            val productPrice: Double = 0.0,
            var quantity: Int = 0,
            val productImage: String = ""
        )

        continueButton.setOnClickListener {
            val userId: String = uid ?: ""
            Log.d(ContentValues.TAG, "User ID: $userId")


            val userCartRef = cartRef.child(userId).child("user_cart")
            val orderProductsRef = ordersRef.push().child("order_products")

            Log.d(ContentValues.TAG, "User ID: $userCartRef")

            userCartRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        // Get product data
                        val productData = productSnapshot.getValue(CartItem2::class.java)
                        Log.d(ContentValues.TAG, "User ID: $productData")

                        if (productData != null) {
                            // Copy the product to the order_products without creating a new auto-generated ID
                            orderProductsRef.child(productSnapshot.key ?: "").setValue(productData)
                        } else {
                            Log.e(ContentValues.TAG, "Product Data is null")
                        }
                    }
                }

                // ** Seems like it doesn't retrieve any value from the user_cart, how can we fix that ? ** //

                override fun onCancelled(error: DatabaseError) {
                    Log.e(ContentValues.TAG, "Firebase Error22: ${error.message}")
                }
            })
        }

    }
}
