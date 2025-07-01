package com.example.project_2_ecommerce_app.PAGES

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.project_2_ecommerce_a.GlobNavigation.navController
import com.example.project_2_ecommerce_app.model.CartItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@Composable
fun CartPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }

    // Fetch cart items from Firestore
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect
        val db = Firebase.firestore

        val userDoc = db.collection("users").document(uid)
        userDoc.get().addOnSuccessListener { snapshot ->
            val cartMap = snapshot.get("cartItems") as? Map<String, Long> ?: emptyMap()

            val productsRef = db.collection("data").document("products").collection("ownproducts")
            productsRef.get().addOnSuccessListener { result ->
                val items = result.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val id = doc.id
                    val quantity = cartMap[id]?.toInt() ?: return@mapNotNull null
                    CartItem(
                        productId = id,
                        title = data["title"].toString(),
                        price = data["actualprice"].toString(),
                        image = data["image"] as? List<String> ?: emptyList(),
                        quantity = quantity
                    )
                }
                cartItems = items
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text("My Cart", fontSize = 20.sp, modifier = Modifier.padding(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems) { item ->
                CartItemView(item, onQuantityChange = { delta ->
                    val newQty = (item.quantity + delta).coerceAtLeast(1)
                    updateCartQuantity(item.productId, newQty, context)
                    cartItems = cartItems.map {
                        if (it.productId == item.productId) it.copy(quantity = newQty) else it
                    }
                }, onRemove = {
                    removeItemFromCart(item.productId, context)
                    cartItems = cartItems.filter { it.productId != item.productId }
                })
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        val total = cartItems.sumOf {
            (it.price.toIntOrNull() ?: 0) * it.quantity
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("₹$total", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
        }

        Button(
            onClick = {
                navController.navigate("checkout")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
        ) {
            Text("Proceed to Checkout")
        }
    }
}

@Composable
fun CartItemView(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.image.firstOrNull()),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontSize = 16.sp, maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Text("₹${item.price}", fontSize = 14.sp, color = Color(0xFF2E7D32))
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onQuantityChange(-1) },
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.Black),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                    ) {
                        Text("-", fontSize = 24.sp)
                    }

                    Text(
                        text = "${item.quantity}",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )

                    OutlinedButton(
                        onClick = { onQuantityChange(1) },
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.Black),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                    ) {
                        Text("+", fontSize = 20.sp)
                    }
                }

            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}
fun updateCartQuantity(productId: String, qty: Int, context: Context) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    Firebase.firestore.collection("users")
        .document(uid)
        .update(mapOf("cartItems.$productId" to qty))
        .addOnFailureListener {
            Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show()
        }
}

fun removeItemFromCart(productId: String, context: Context) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    Firebase.firestore.collection("users")
        .document(uid)
        .update(mapOf("cartItems.$productId" to FieldValue.delete()))
        .addOnSuccessListener {
            Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
        }
}
