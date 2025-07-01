package com.example.project_2_ecommerce_app.model

data class CartItem(
    val productId: String = "",
    val title: String = "",
    val price: String = "", // MRP
    val actualprice: String = "", // Final discounted price
    val image: List<String> = emptyList(),
    val quantity: Int = 0
)
