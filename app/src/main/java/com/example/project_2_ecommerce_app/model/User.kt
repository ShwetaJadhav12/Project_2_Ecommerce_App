package com.example.project_2_ecommerce_app.model

data class User(
    val name: String,
    val email: String,
    val userId: String?,

//    we will not store password because it will be handled by authentication
)
