package com.example.techiess

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Orders : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.ordersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = OrdersAdapter(emptyList()) // Pass an empty list initially
        recyclerView.adapter = ordersAdapter

        // Retrieve data from Firebase
        retrieveOrdersFromFirebase()
    }

    private fun retrieveOrdersFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val userId: String = uid ?: ""

        Log.d("Firebase", "User ID: $userId")

        val ordersRef = db.collection("orders").document(userId).collection("order_id")

        ordersRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle error
                Log.e("Firebase", "Error fetching orders: ${exception.message}")
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                lifecycleScope.launch(Dispatchers.IO) {
                    // Use async to fetch order details concurrently
                    val deferredOrders = snapshot.documents.map { orderDocument ->
                        val orderId = orderDocument.id
                        fetchOrderDetails(orderId)
                    }

                    // Wait for all async calls to complete
                    val ordersList = deferredOrders.awaitAll()

                    // Update the RecyclerView with the complete list of orders on the main thread
                    withContext(Dispatchers.Main) {
                        ordersAdapter.updateData(ordersList)
                    }
                }
            } else {
                // No orders data
                Log.d("Firebase", "No orders data")
                // Note: Consider implementing logic for handling no data
            }
        }
    }



    private fun fetchOrderDetails(orderId: String): Deferred<Order> = lifecycleScope.async(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        val orderProductsRef = db.collection("orders")
            .document(userId)
            .collection("order_id")
            .document(orderId)
            .collection("order_product")

        val productSnapshot = orderProductsRef.get().await()

        if (productSnapshot != null && !productSnapshot.isEmpty) {
            val items = productSnapshot.documents.mapNotNull { productDoc ->
                productDoc.toObject(CartItem::class.java)
            }
            Order(orderId, items)
        } else {
            // No order products data
            Log.d("Firebase", "No order products data for order $orderId")
            // Note: Consider implementing logic for handling no data
            Order(orderId, emptyList())
        }
    }

}
