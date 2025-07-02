package com.example.project_2_ecommerce_app.model

data class OwnProducts(
    val title: String? = null,
    val price: String? = null,
    val actualprice: String? = "0",
    val brand: String? = null,
    val category: String? = null,
    val description: String? = null,
    var id: String? = null,
    val image: List<String>? = null
)
