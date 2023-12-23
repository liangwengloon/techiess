package com.example.techiess.ui.cart

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
import com.example.techiess.R
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment(), CartAdapter.CartItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter

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
            val documentId = currentItem.id


        // This is not showing any document ID , how to retrieve itself ID
            Log.d("CartFragment", "Deleting item at position $position with documentId: $documentId")

            cartReference.document(documentId).delete()
                .addOnSuccessListener {
                    Log.d("CartFragment", "Item deleted successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("CartFragment", "Failed to delete item", e)
                }
        }
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
                } else {
                    // No data
                    // You can show a message indicating an empty cart
                }
            }
        }
    }
}
