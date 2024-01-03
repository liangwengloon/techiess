package com.example.techiess.ui.home

import java.io.Serializable

data class Product(
    val title: String = "",
    val price: Double = 0.0,
    val imageResId: String = "",
    val category: String = "",
    val OS: String = "",
    val battery: String = "",
    val camera: String = "",
    val desc: String = "",
    val display: String = "",
    val ram: String = "",
    val rom: String = "",
    val productID: String = "",
    var documentID: String = ""
) : Serializable

