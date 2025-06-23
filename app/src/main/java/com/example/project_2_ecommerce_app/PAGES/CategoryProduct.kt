package com.example.project_2_ecommerce_app.PAGES

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.project_2_ecommerce_app.model.OwnProducts
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProduct(modifier: Modifier = Modifier, categoryId: String) {


    var productList by remember { mutableStateOf<List<OwnProducts>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("products")
            .collection("ownproducts")
            .get()
            .addOnSuccessListener {
                result ->
                val products = result.documents.mapNotNull {
                    doc ->
                    doc.toObject(OwnProducts::class.java)
                }
                productList = products
            }
            .addOnFailureListener {
                println("Error fetching products: ${it.message}")
            }
    }

    LazyColumn {
        items(productList) { product ->
            Text(text = product.title ?: "No Name")
        }
    }
}
