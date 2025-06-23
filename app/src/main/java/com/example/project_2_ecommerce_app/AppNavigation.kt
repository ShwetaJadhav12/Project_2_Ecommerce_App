package com.example.project_2_ecommerce_app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_2_ecommerce_app.PAGES.CategoryProduct
import com.example.project_2_ecommerce_app.components.ProductDetailScreen
import com.example.project_2_ecommerce_app.model.OwnProducts
import com.example.project_2_ecommerce_app.screen.AuthScreen
import com.example.project_2_ecommerce_app.screen.HomeScreen
import com.example.project_2_ecommerce_app.screen.LoginScreen
import com.example.project_2_ecommerce_app.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    globNavigation.navController = navController
    val isLoggedIn = Firebase.auth.currentUser!= null
    val firstPage = if(isLoggedIn) "home" else "auth"
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = firstPage
    ){
        composable("auth"){
            AuthScreen(navController)
        }
        composable("login"){
            LoginScreen(navController)
        }
        composable("signup"){
            SignupScreen(navController)
        }
        composable("home"){
            HomeScreen(navController)
        }
        composable("categoryProduct/{categoryId}"){
            CategoryProduct(modifier, it.arguments?.getString("categoryId") ?: "")

        }
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val selectedProduct = remember { mutableStateOf<OwnProducts?>(null) }

            LaunchedEffect(productId) {
                com.google.firebase.Firebase.firestore
                    .collection("data")
                    .document("products")
                    .collection("ownproducts")
                    .document(productId)
                    .get()
                    .addOnSuccessListener { document ->
                        val product = document.toObject(OwnProducts::class.java)
                        selectedProduct.value = product
                    }
                    .addOnFailureListener {
                        println("Failed to load product details: ${it.message}")
                    }
            }

            selectedProduct.value?.let { product ->
                ProductDetailScreen(
                    product = product,
                    onBackClick = { navController.popBackStack() }
                )
            } ?: run {
                // Optional loading text or shimmer
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }



    }
}
object globNavigation {

    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavHostController
}

