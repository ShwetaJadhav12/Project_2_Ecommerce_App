package com.example.project_2_ecommerce_app.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project_2_ecommerce_app.model.OwnProducts
import androidx.compose.foundation.clickable

@Composable
fun MostSellerItem(
    product: OwnProducts,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)) // Light background
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.image?.firstOrNull(),
                contentDescription = product.title ?: "Product Image",
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = product.title ?: "No Title",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "₹${product.price ?: "0"}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF490549)
            )
        }
    }
}
