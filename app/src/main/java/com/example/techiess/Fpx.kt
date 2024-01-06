package com.example.techiess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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


        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid ?: ""
        deleteAllUserCartDocuments(uid)

    }


    private fun deleteAllUserCartDocuments(userId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the user_cart collection for the specific user
        val userCartRef = db.collection("cart").document(userId).collection("user_cart")

        // Delete all documents in the user_cart collection
        userCartRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            // Document successfully deleted
                            println("Document deleted: ${document.id}")
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors during deletion
                            println("Error deleting document: ${document.id}, $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle any errors during fetching the documents
                println("Error fetching user_cart documents: $e")
            }
    }

}