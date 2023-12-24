package com.example.techiess.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techiess.CartAdapter
import com.example.techiess.CartItem
import com.example.techiess.Checkout
import com.example.techiess.R
import com.example.techiess.signUp
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment(), CartAdapter.CartItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var btnCheckout: Button
    private lateinit var tvTotalAmount: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = root.findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter

        // Set click listener for the adapter
        cartAdapter.setCartItemClickListener(this)

        btnCheckout = root.findViewById(R.id.btnCheckout)
        tvTotalAmount = root.findViewById(R.id.tvTotalAmount)

       // fetchProductDataAndCalculateTotalAmount()

        // Set click listener for the checkout button
        btnCheckout.setOnClickListener {


            val intent = Intent(requireContext(), Checkout::class.java)
            startActivity(intent)
            // Navigate to the checkout activity
        }

        // Load user's cart data
        loadUserCartData()

        return root
    }

    override fun onPlusButtonClick(position: Int) {
        // Increase quantity in the CartItem object and update Firebase
        val currentItem = cartAdapter.getItem(position)
        updateQuantity(currentItem, 1)
        Log.d("CartFragment", " The current Item $currentItem")


    }

    private fun fetchProductDataAndCalculateTotalAmount() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            val cartReference =
                FirebaseFirestore.getInstance().collection("cart").document(uid)
                    .collection("user_cart")

            cartReference.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val cartItems = task.result?.toObjects(CartItem::class.java)

                    if (cartItems != null && cartItems.isNotEmpty()) {
                        // Calculate total amount
                        val totalAmount = calculateTotalAmount(cartItems)
                        tvTotalAmount.text = "Total: $totalAmount"

                        // Navigate to the checkout activity

                    } else {
                        Log.d("CartFragment", "No items in the cart.")
                    }
                } else {
                    Log.e("CartFragment", "Error getting cart items: ", task.exception)
                }
            }
        }
    }
    private fun calculateTotalAmount(cartItems: List<CartItem>): Double {
        var totalAmount = 0.0

        // Iterate through cart items and calculate total amount
        for (cartItem in cartItems) {
            totalAmount += cartItem.productPrice * cartItem.quantity
            Log.d("Price", " The current Item $totalAmount")
        }

        return totalAmount
    }



    override fun onMinusButtonClick(position: Int) {
        // Decrease quantity in the CartItem object and update Firebase
        val currentItem = cartAdapter.getItem(position)
        if (currentItem.quantity > 1) {
            updateQuantity(currentItem, -1)


        }
    }

    override fun onDeleteButtonClick(position: Int) {
        // Delete the item from Firebase
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            val cartReference =
                FirebaseFirestore.getInstance().collection("cart").document(uid!!)
                    .collection("user_cart")

            val currentItem = cartAdapter.getItem(position)

            // Query to find the specific document based on productId
            val query = cartReference.whereEqualTo("productID", currentItem.productID)

            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        // Found the document, get the first document snapshot
                        val documentSnapshot = documents[0]

                        // Now you have the document snapshot, you can access its data or perform other operations

                        // Delete the document
                        documentSnapshot.reference.delete()
                            .addOnSuccessListener {
                                Log.d("CartFragment", "Item deleted successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.e("CartFragment", "Failed to delete item", e)
                            }
                    } else {
                        Log.d("CartFragment", "Document not found for product ID: ${currentItem.productID}")
                    }
                } else {
                    Log.e("CartFragment", "Error getting documents: ", task.exception)
                }
            }
        }
    }


    private fun navigateToCheckout(totalAmount: Double) {
        // Create an Intent to start the CheckoutActivity
        val intent = Intent(requireContext(), Checkout::class.java)

        // Put the necessary data into the Intent
        intent.putExtra("total_amount", totalAmount)

        // Start the CheckoutActivity
        startActivity(intent)
    }


    private fun updateQuantity(cartItem: CartItem, quantityChange: Int) {
        // Update the quantity for the item in Firebase
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            val cartReference =
                FirebaseFirestore.getInstance().collection("cart").document(uid)
                    .collection("user_cart")

            // Use the productId directly from the currentItem
            val productId = cartItem.productID

            Log.d("CartFragment", "Document not found for product ID: $productId")

            // Query to find the specific document based on productId
            val query = cartReference.whereEqualTo("productID", productId)

            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        // Found the document, update the quantity
                        val documentId = documents[0].id
                        val existingQuantity = documents[0].getLong("quantity")?.toInt() ?: 0
                        val newQuantity = existingQuantity + quantityChange

                        if (newQuantity > 0) {
                            // Update the quantity only if it's a positive value
                            cartReference.document(documentId).update("quantity", newQuantity)
                                .addOnSuccessListener {
                                    Log.d(
                                        "CartFragment",
                                        "Quantity updated to $newQuantity for document $documentId"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        "CartFragment",
                                        "Failed to update quantity for document $documentId",
                                        e
                                    )
                                }
                        } else {
                            Log.d("CartFragment", "Invalid quantity after update: $newQuantity")
                        }
                    } else {
                        Log.d("CartFragment", "Document not found for product ID: $productId")
                    }
                } else {
                    Log.e("CartFragment", "Error getting documents: ", task.exception)
                }
            }
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
                    cartAdapter.setItems(cartItems)
                    fetchProductDataAndCalculateTotalAmount()

                } else {
                    // No data
                    // You can show a message indicating an empty cart
                }
            }
        }
    }
}
