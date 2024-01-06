package com.example.techiess

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.techiess.databinding.ActivityInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore



class Information : AppCompatActivity() {

    private lateinit var binding: ActivityInformationBinding
    private val paymentMethods by lazy {
        resources.getStringArray(R.array.payment_methods)
    }

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

        continueButton.setOnClickListener {
            createOrder()

        }
        fetchAndUpdateUserInfo()
    }

    private fun createOrder() {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val userId: String = uid ?: ""

        // Retrieve user information from input fields
        val userName = binding.etName.text.toString()
        val userPhone = binding.etPhone.text.toString()
        val userAddressLine1 = binding.etAddressLine1.text.toString()
        val userAddressLine2 = binding.etAddressLine2.text.toString()
        val userPostcode = binding.etPostcode.text.toString()
        val selectedPaymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()

        // Create a map with user information
        val userInfo = mapOf(
            "name" to userName,
            "phone" to userPhone,
            "addressLine1" to userAddressLine1,
            "addressLine2" to userAddressLine2,
            "postcode" to userPostcode,
            "paymentMethod" to selectedPaymentMethod
        )

        // Insert user information under the "users" collection in Firestore
        db.collection("users").document(userId)
            .set(userInfo)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information added to Firestore for user $userId")

                // Continue with the Orders creation logic
                createOrderDocument(userId)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding user information to Firestore", e)
            }
    }

    private fun createOrderDocument(userId: String) {
        val db = FirebaseFirestore.getInstance()

        // Create a new order document under "orders" collection
        val orderDocumentRef = db.collection("orders").document(userId).collection("order_id").document()

        val orderData = mapOf("dummy" to "data") // You can customize this data as needed

        orderDocumentRef.set(orderData)
            .addOnSuccessListener {
                val orderId = orderDocumentRef.id
                Log.d(ContentValues.TAG, "Order document created for user $userId with ordersId $orderId")

                // Now, copy user_cart to order_products
                copyUserCartToOrderProducts(userId, orderId)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error creating Orders document", e)
            }
    }

    private fun copyUserCartToOrderProducts(userId: String, orderId: String) {
        val db = FirebaseFirestore.getInstance()

        // Retrieve user_cart
        val userCartRef = db.collection("cart").document(userId).collection("user_cart")
        userCartRef.get()
            .addOnSuccessListener { cartSnapshot ->
                for (cartDocument in cartSnapshot.documents) {
                    val cartItem = cartDocument.toObject(CartItem::class.java)
                    if (cartItem != null) {
                        // Copy cart item to order_products
                        db.collection("orders").document(userId)
                            .collection("order_id").document(orderId)
                            .collection("order_product").add(cartItem)
                            .addOnSuccessListener { productDocumentReference ->
                                val productId = productDocumentReference.id
                                Log.d(ContentValues.TAG, "Product copied to order_products with productId $productId")
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error copying product to order_products", e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error fetching user_cart", e)
            }

        // Navigate to the Fpx activity
        navigateToFpxActivity()
    }

    private fun fetchAndUpdateUserInfo() {

        data class UserInfo(
            val name: String = "",
            val phone: String = "",
            val addressLine1: String = "",
            val addressLine2: String = "",
            val postcode: String = "",
            val paymentMethod: String = ""
        ) {
            // Add a no-argument constructor
            constructor() : this("", "", "", "", "", "")
        }



        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid ?: ""

        // Retrieve user information from Firestore
        val userDocRef = db.collection("users").document(uid)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userInfo = documentSnapshot.toObject(UserInfo::class.java)
                    if (userInfo != null) {
                        // Update EditText fields with user information
                        binding.etName.setText(userInfo.name)
                        binding.etPhone.setText(userInfo.phone)
                        binding.etAddressLine1.setText(userInfo.addressLine1)
                        binding.etAddressLine2.setText(userInfo.addressLine2)
                        binding.etPostcode.setText(userInfo.postcode)

                        // Set the selected payment method in the spinner
                        val paymentMethodIndex = paymentMethods.indexOf(userInfo.paymentMethod)
                        if (paymentMethodIndex != -1) {
                            binding.spinnerPaymentMethod.setSelection(paymentMethodIndex)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error fetching user information from Firestore", e)
            }
    }


    private fun navigateToFpxActivity() {
        val intent = Intent(this, Fpx::class.java)
        startActivity(intent)
        finish()
    }
}
