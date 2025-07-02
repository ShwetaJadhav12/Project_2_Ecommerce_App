package com.example.project_2_ecommerce_app.PAGES


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.project_2_ecommerce_app.firebase.FavoritesManager
import com.example.project_2_ecommerce_app.model.OwnProducts
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun ProductCard(
    product: OwnProducts,
    onClick: () -> Unit,
    showLike: Boolean = true
) {
    var liked by remember { mutableStateOf(false) }

    LaunchedEffect(product.id) {
        FavoritesManager.isFavorite(product.id ?: "") {
            liked = it
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD))
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.image?.firstOrNull()),
                contentDescription = product.title ?: "Product Image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = product.title ?: "Product Title",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${product.price ?: "--"}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    )

                    if (!product.actualprice.isNullOrBlank() && product.actualprice != product.price) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹${product.actualprice}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }
                }
            }

            if (showLike) {
                IconButton(onClick = {
                    liked = !liked
                    val pid = product.id ?: return@IconButton
                    if (liked) {
                        FavoritesManager.addToFavorites(pid)
                    } else {
                        FavoritesManager.removeFromFavorites(pid)
                    }
                }) {
                    Icon(
                        imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (liked) Color(0xFFE91E63) else Color.Gray
                    )
                }
            }
        }
    }
}



@Composable
fun FavoriteProductsScreen(modifier: Modifier = Modifier, onProductClick: (String) -> Unit) {
    var favoriteProducts by remember { mutableStateOf<List<OwnProducts>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        FavoritesManager.getAllFavorites { productIds ->
            if (productIds.isEmpty()) {
                isLoading = false
                return@getAllFavorites
            }

            val db = Firebase.firestore
            val tempList = mutableListOf<OwnProducts>()
            var loadedCount = 0

            productIds.forEach { id ->
                db.collection("data")
                    .document("products")
                    .collection("ownproducts")
                    .document(id)
                    .get()
                    .addOnSuccessListener { document ->
                        val product = document.toObject(OwnProducts::class.java)
                        if (product != null) {
                            tempList.add(product)
                        }
                        loadedCount++
                        if (loadedCount == productIds.size) {
                            favoriteProducts = tempList
                            isLoading = false
                        }
                    }
                    .addOnFailureListener {
                        loadedCount++
                        if (loadedCount == productIds.size) {
                            favoriteProducts = tempList
                            isLoading = false
                        }
                    }
            }
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (favoriteProducts.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No favorites yet.")
        }
    } else {
        LazyColumn(modifier = modifier.padding(8.dp)) {
            items(favoriteProducts) { product ->
                ProductCard(product = product, onClick = {
                    onProductClick(product.id ?: "")
                })
            }
        }
    }
}
