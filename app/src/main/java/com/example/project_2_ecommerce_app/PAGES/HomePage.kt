package com.example.project_2_ecommerce_app.PAGES

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_2_ecommerce_app.composable.BannerView
import com.example.project_2_ecommerce_app.composable.HeaderView


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


    }

}