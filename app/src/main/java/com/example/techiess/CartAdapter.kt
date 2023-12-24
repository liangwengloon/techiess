package com.example.techiess

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<CartItem> = emptyList()

    // ViewHolder class

    interface CartItemClickListener {
        fun onPlusButtonClick(position: Int)
        fun onMinusButtonClick(position: Int)
        fun onDeleteButtonClick(position: Int)
    }
    private var clickListener: CartItemClickListener? = null

    fun setCartItemClickListener(listener: CartItemClickListener) {
        clickListener = listener
    }



    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.imageProduct)
        val productName: TextView = itemView.findViewById(R.id.textProductName)
        val productPrice: TextView = itemView.findViewById(R.id.textProductPrice)
        val quantity: TextView = itemView.findViewById(R.id.textQuantity)
        val plusButton: Button = itemView.findViewById(R.id.plusButton)
        val minusButton: Button = itemView.findViewById(R.id.minusButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    // Set items to the adapter
    fun setItems(items: List<CartItem>) {
        cartItems = items
        notifyDataSetChanged()
    }
    fun getItem(position: Int): CartItem {
        return cartItems[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]

        Log.d("CartAdapter", "Image URL: ${currentItem.productImage}")
        // Bind data to UI element//
        Log.d("CartAdapter", "Image URL: ${currentItem.productID}")

        holder.productName.text = currentItem.productName
        holder.productPrice.text = "Price: ${currentItem.productPrice.toString()}"
        holder.quantity.text = "${currentItem.quantity}"


        Glide.with(holder.itemView.context)
            .load(currentItem.productImage)
            .override(300, 300)  // Set the desired width and height
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

        holder.plusButton.setOnClickListener {
            clickListener?.onPlusButtonClick(position)
        }

        holder.minusButton.setOnClickListener {
            clickListener?.onMinusButtonClick(position)
        }

        holder.deleteButton.setOnClickListener {
            clickListener?.onDeleteButtonClick(position)
        }
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