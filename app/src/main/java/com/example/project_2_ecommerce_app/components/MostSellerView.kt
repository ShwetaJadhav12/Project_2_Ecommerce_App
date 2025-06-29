package com.example.project_2_ecommerce_app.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_2_ecommerce_app.globNavigation.navController
import com.example.project_2_ecommerce_app.model.OwnProducts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun MostSellerView() {
    val db = Firebase.firestore
    var productList by remember { mutableStateOf(listOf<OwnProducts>()) }

    LaunchedEffect(Unit) {
        db.collection("data")
            .document("products")
            .collection("mostseller")
            .get()
            .addOnSuccessListener { snapshot ->
                val ids = snapshot.mapNotNull { it.getString("productId") }

                ids.forEach { id ->
                    db.collection("data")
                        .document("products")
                        .collection("ownproducts")
                        .document(id)
                        .get()
                        .addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                val product = doc.toObject(OwnProducts::class.java)
                                product?.let {
                                    productList = productList + it
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("MostSellerView", "Failed to fetch product: $id", it)
                        }
                }
            }
            .addOnFailureListener {
                Log.e("MostSellerView", "Failed to fetch mostseller list", it)
            }
    }

    if (productList.isEmpty()) {
        Text("Loading Most Seller...", modifier = Modifier.padding(16.dp))
    } else {
        LazyRow(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        ) {
            items(productList) { product ->
                MostSellerItem(product = product) {
                    navController.navigate("productDetail/${product.id}")
                }
            }
        }
    }
}
