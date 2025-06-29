package com.example.project_2_ecommerce_app.PAGES

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_2_ecommerce_app.components.ProductItemView
import com.example.project_2_ecommerce_app.globNavigation.navController
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
            .whereEqualTo("category", categoryId)
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull { doc ->
                    doc.toObject(OwnProducts::class.java)?.copy(id = doc.id)
                }
                productList = products
            }
            .addOnFailureListener {
                println("Error fetching products: ${it.message}")
            }
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(productList) { product ->
            ProductItemView(
                modifier = Modifier.fillMaxWidth(),
                product = product,
                onClick = { navController.navigate("productDetail/${product.id}") },
            )
        }
    }
}
