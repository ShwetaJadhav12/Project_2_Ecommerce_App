package com.example.project_2_ecommerce_app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project_2_ecommerce_app.globNavigation
import com.example.project_2_ecommerce_app.model.Category
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryView(modifier: Modifier = Modifier){
    var categoryList by remember { mutableStateOf<List<Category>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("products")
            .collection("categories")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val rcategories = it.result.documents.mapNotNull { doc ->
                        doc.toObject(Category::class.java)
                    }
                    categoryList = rcategories
                }
            }


    }
    LazyRow() {
        items(categoryList.size) { index ->
            CategoryItem(category = categoryList[index])

        }

    }

}


@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category
) {
    val c1 = Color(0xFF039BE5)
    Card(
        modifier = modifier
            .padding(6.dp)
            .width(120.dp)
            .height(124.dp).clickable {
                globNavigation.navController.navigate("categoryProduct/${category.id}")

            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(  containerColor =  Color.White)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier
                    .size(95.dp)
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }
    }
}
