package com.example.project_2_ecommerce_a
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_2_ecommerce_app.PAGES.CategoryProduct
import com.example.project_2_ecommerce_app.PAGES.CheckOutPage
import com.example.project_2_ecommerce_app.PAGES.FavoriteProductsScreen
import com.example.project_2_ecommerce_app.components.ProductDetailScreenFromId
import com.example.project_2_ecommerce_app.PAGES.ProfilePage
import com.example.project_2_ecommerce_app.screen.AuthScreen
import com.example.project_2_ecommerce_app.screen.HomeScreen
import com.example.project_2_ecommerce_app.screen.LoginScreen
import com.example.project_2_ecommerce_app.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    GlobNavigation.navController = navController
    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if (isLoggedIn) "home" else "auth"

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = firstPage
    ) {
        composable("auth") {
            AuthScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("categoryProduct/{categoryId}") {
            val categoryId = it.arguments?.getString("categoryId") ?: ""
            CategoryProduct(modifier, categoryId)
        }
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreenFromId(productId = productId) {
                navController.popBackStack()
            }
        }
        composable("profile") {
            ProfilePage(modifier)
        }
        composable("favorites") {
            FavoriteProductsScreen(
                onProductClick = { productId ->
                    navController.navigate("productDetail/$productId")
                }, modifier = modifier
            )
        }
        composable("checkout") {
            CheckOutPage(modifier)
        }


    }
}

object GlobNavigation {
    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavHostController
}
