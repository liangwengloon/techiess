package com.example.techiess

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class SimpleCartAdapter : RecyclerView.Adapter<SimpleCartAdapter.SimpleCartViewHolder>() {

    private var cartItems: List<CartItem> = emptyList()

    class SimpleCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.imageProduct)
        val productName: TextView = itemView.findViewById(R.id.textProductName)
        val productPrice: TextView = itemView.findViewById(R.id.textProductPrice)
        val quantity: TextView = itemView.findViewById(R.id.textQuantity)
    }

    // Set items to the adapter
    fun setItems(items: List<CartItem>) {
        cartItems = items
        notifyDataSetChanged()
    }

    fun getItem(position: Int): CartItem {
        return cartItems[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleCartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_cart, parent, false)
        return SimpleCartViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleCartViewHolder, position: Int) {
        val currentItem = cartItems[position]

        Log.d("SimpleCartAdapter", "Image URL: ${currentItem.productImage}")
        Log.d("SimpleCartAdapter", "Image URL: ${currentItem.productID}")

        holder.productName.text = currentItem.productName
        holder.productPrice.text = "RM: ${currentItem.productPrice.toString()}"
        holder.quantity.text = "Qty: ${currentItem.quantity}"

        Glide.with(holder.itemView.context)
            .load(currentItem.productImage)
            .override(300, 300)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Glide", "Load failed", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(holder.productImage)
    }

    fun getItems(): List<CartItem> {
        return cartItems
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun getItemId(position: Int): Long {
        return cartItems[position].id.hashCode().toLong()
    }
}
