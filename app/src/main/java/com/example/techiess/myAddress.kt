package com.example.techiess


import android.app.AlertDialog
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
import com.example.techiess.CartItem
import com.example.techiess.Checkout
import com.example.techiess.Fpx
import com.example.techiess.Home
import com.example.techiess.R
import com.example.techiess.databinding.ActivityInformationBinding
import com.example.techiess.databinding.ActivityMyAddressBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore



class myAddress : AppCompatActivity() {

    private lateinit var binding: ActivityMyAddressBinding
    private val paymentMethods by lazy {
        resources.getStringArray(R.array.payment_methods)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
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

        // Create a map with user information
        val userInfo = mapOf(
            "name" to userName,
            "phone" to userPhone,
            "addressLine1" to userAddressLine1,
            "addressLine2" to userAddressLine2,
            "postcode" to userPostcode,
        )

        // Insert user information under the "users" collection in Firestore
        db.collection("users").document(userId)
            .set(userInfo)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information added to Firestore for user $userId")
                showSuccessDialog()


            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding user information to Firestore", e)
                showErrorDialog()

            }
    }

    private fun showSuccessDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Success")
        alertDialogBuilder.setMessage("Address has been updated successfully.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            // Handle positive button click if needed
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    private fun showErrorDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage("Failed to update address. Please try again.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            // Handle positive button click if needed
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }
    private fun fetchAndUpdateUserInfo() {

        data class UserInfo(
            val name: String = "",
            val phone: String = "",
            val addressLine1: String = "",
            val addressLine2: String = "",
            val postcode: String = "",

        ) {
            // Add a no-argument constructor
            constructor() : this("", "", "", "", "")
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

                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error fetching user information from Firestore", e)
            }
    }


    private fun navigateToHomeActivity() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}
