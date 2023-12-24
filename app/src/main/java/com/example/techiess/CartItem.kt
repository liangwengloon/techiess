package com.example.techiess

data class CartItem(
    val id: String ="",
    val productID: String = "",
    val productName: String = "",
    val productPrice: Double = 0.0,
    var quantity: Int = 0,
    val productImage: String = ""
)
