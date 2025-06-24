package com.example.project_2_ecommerce_app.model

data class CartItem(
    val productId: String = "",
    val title: String = "",
    val price: String = "",
    val image: List<String> = emptyList(),
    var quantity: Int = 1
)
