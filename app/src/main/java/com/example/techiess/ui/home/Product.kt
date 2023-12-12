package com.example.techiess.ui.home

data class Product(
    val title: String,
    val price: String,
    val imageResId: String = "",
    val category: String,
    val OS: String = "",
    val battery: String = "",
    val camera: String = "",
    val desc: String = "",
    val display: String = "",
    val ram: String = "",
    val rom: String = "",
    val productID: String = ""
)
