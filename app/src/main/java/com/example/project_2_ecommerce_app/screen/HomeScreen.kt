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

//    it will remember the last selected index
//    means on which page we wre at the last
//    so that when we clicked on back it will go to the last page we visited not the home page
    var selectedIndex by rememberSaveable  { mutableStateOf(0) }

    Scaffold(
        containerColor = Color(0xFFE5E2E2),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 10.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ){ innerPadding ->
            ContentScreen(modifier = Modifier.padding(innerPadding),selectedIndex)
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