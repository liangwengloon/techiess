package com.example.techiess

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Picasso
import com.bumptech.glide.load.DataSource

class OrdersAdapter(private var ordersList: List<Order>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOrderID: TextView = itemView.findViewById(R.id.textOrderID)
        val textProductDetails: TextView = itemView.findViewById(R.id.textProductDetails)
        val imagesRecyclerViewContainer: LinearLayout = itemView.findViewById(R.id.imagesRecyclerViewContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_id, parent, false)
        return OrderViewHolder(view)
    }

    fun updateData(newOrdersList: List<Order>) {
        ordersList = newOrdersList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentOrder = ordersList[position]

        // Set order ID to the text view
        holder.textOrderID.text = "Order ID: ${currentOrder.orderId}"

        // Set the product details
        val productDetailsBuilder = StringBuilder()
        for ((productName, items) in currentOrder.cartItems.groupBy { it.productName }) {
            // Display product name
            productDetailsBuilder.append("$productName\n")

            // Display total price for the product
            val totalPrice = items.sumByDouble { it.productPrice * it.quantity }
            productDetailsBuilder.append("Price: $totalPrice\n")

            // Display total quantity for the product
            val totalQuantity = items.sumBy { it.quantity }
            productDetailsBuilder.append("Quantity: $totalQuantity\n\n\n\n")
        }

        val imagesRecyclerView = RecyclerView(holder.itemView.context)
        imagesRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        val imageUrls = currentOrder.cartItems.flatMap { listOf(it.productImage) }

        val imageAdapter = ProductImageAdapter(imageUrls)
        imagesRecyclerView.adapter = imageAdapter

        holder.imagesRecyclerViewContainer.removeAllViews()
        holder.imagesRecyclerViewContainer.addView(imagesRecyclerView)


        holder.textProductDetails.text = productDetailsBuilder.toString()
    }


    override fun getItemCount(): Int {
        return ordersList.size
    }
}

