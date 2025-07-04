package com.example.project_2_ecommerce_app.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_2_ecommerce_a.GlobNavigation.navController
import com.example.project_2_ecommerce_app.PAGES.CartPage
import com.example.project_2_ecommerce_app.PAGES.FavoriteProductsScreen
import com.example.project_2_ecommerce_app.PAGES.HomePage
import com.example.project_2_ecommerce_app.PAGES.ProfilePage

@Composable
fun HomeScreen(navController: NavController) {
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Cart", Icons.Default.ShoppingCart),
        NavItem("Profile", Icons.Default.Person)
    )

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        containerColor = Color(0xFFF8F9FA), // Light gray background

        bottomBar = {
            // Wrap NavigationBar in a Box to control height
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(95.dp) // ↓ Change this to desired height
            ) {
                NavigationBar(
                    modifier = Modifier.fillMaxSize(), // Fill the Box
                    containerColor = Color(0xFF3B458A), // Dark blue
                    tonalElevation = 8.dp
                ) {
                    navItems.forEachIndexed { index, item ->
                        val isSelected = selectedIndex == index

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedIndex = index },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = if (isSelected) Color(0xFF0D1333) else Color.White
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    color = if (isSelected) Color(0xFFFFF8E1) else Color.White
                                )
                            },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        }
    )
    { innerPadding ->
        ContentScreen(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                ,
            selectedIndex = selectedIndex
        )
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier,selectedIndex: Int){
    when(selectedIndex)
    {
        0 -> HomePage(modifier)
        1 -> FavoriteProductsScreen(
            onProductClick = { productId ->
                navController.navigate("productDetail/$productId")
            }, modifier = modifier
        )
        2 -> CartPage(modifier)
        3 -> ProfilePage(modifier)
    }
}
data class NavItem(
    val label: String,
    val icon: ImageVector
)