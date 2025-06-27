package com.example.project_2_ecommerce_app.PAGES

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_2_ecommerce_app.components.BannerView
import com.example.project_2_ecommerce_app.components.CategoryView
import com.example.project_2_ecommerce_app.components.HeaderView
import com.example.project_2_ecommerce_app.components.MostSellerView


@Composable
fun HomePage(modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxSize().padding(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        )
    ) {
        HeaderView()
        BannerView()
        Text(text ="Categories", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Start,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
            fontStyle = FontStyle.Normal
        ))
        CategoryView()
        Text(
            text = "Most Seller",
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        MostSellerView()



    }

}