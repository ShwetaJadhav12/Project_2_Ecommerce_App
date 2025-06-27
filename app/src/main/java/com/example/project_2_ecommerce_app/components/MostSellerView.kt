package com.example.project_2_ecommerce_app.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_2_ecommerce_app.model.OwnProducts
import com.example.project_2_ecommerce_app.model.productIdRef
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
                val ids = snapshot.mapNotNull {
                    it.getString("productId")
                }


                Log.d("MostSellerView", "Fetched product IDs: $ids")

                ids.forEach { id ->
                    db.collection("data")
                        .document("products")
                        .collection("ownproducts")
                        .document(id)
                        .get()
                        .addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                Log.d("MostSellerView", "Document exists for id: $id")

                                // Print raw data
                                Log.d("MostSellerView", "Raw data: ${doc.data}")

                                val product = doc.toObject(OwnProducts::class.java)
                                Log.d("MostSellerView", "Loaded product: $product")

                                product?.let {
                                    productList = productList + it
                                }
                            } else {
                                Log.d("MostSellerView", "No such document: $id")
                            }
                        }

                        .addOnFailureListener {
                            Log.e("MostSellerView", "Failed to fetch product: $id")
                        }
                }
            }
            .addOnFailureListener {
                Log.e("MostSellerView", "Failed to fetch mostseller list")
            }
    }

    if (productList.isEmpty()) {
        Text("Loading Most Seller...", modifier = Modifier.padding(16.dp))
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(productList) { product ->
                MostSellerItem(product)
            }
        }
    }
}
