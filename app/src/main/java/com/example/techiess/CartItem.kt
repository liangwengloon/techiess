package com.example.techiess

data class CartItem(
    val id: String ="",
    val productID: String = "",
    val productName: String = "",
    val productPrice: String = "",
    var quantity: Int = 0,
    val productImage: String = ""
)
