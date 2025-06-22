package com.example.project_2_ecommerce_app.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun BannerView(modifier: Modifier = Modifier) {
    var bannerList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("banners")
            .get()
            .addOnCompleteListener {
                bannerList = it.result.get("urls") as? List<String> ?: emptyList()
            }
    }

    val pagerState = rememberPagerState { bannerList.size }

    if (bannerList.isNotEmpty()) {
        Column(modifier = modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 24.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 8.dp)
            ) { page ->
                AsyncImage(
                    model = bannerList[page],
                    contentDescription = "Banner Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = bannerList.size,
                activeColor = Color(0xFFF8035D),  // Green
                inactiveColor = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) // ✅ Centering the dots
                    .padding(top = 4.dp)
            )
        }
    }
}
