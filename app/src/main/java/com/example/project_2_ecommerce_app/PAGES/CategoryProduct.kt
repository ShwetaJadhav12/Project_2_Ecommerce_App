package com.example.project_2_ecommerce_app.PAGES

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoryProduct(modifier: Modifier = Modifier, categoryId: String){
    Text(text = "Category Product+$categoryId")

}
