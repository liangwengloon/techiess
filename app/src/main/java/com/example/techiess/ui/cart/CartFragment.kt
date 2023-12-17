package com.example.techiess.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techiess.CartAdapter
import com.example.techiess.CartItem
import com.example.techiess.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {

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

        // Load user's cart data
        loadUserCartData()

        return root
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
