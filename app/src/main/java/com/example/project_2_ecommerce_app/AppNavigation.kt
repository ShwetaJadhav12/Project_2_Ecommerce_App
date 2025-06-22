package com.example.project_2_ecommerce_app

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_2_ecommerce_app.PAGES.CategoryProduct
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

    }
}
object globNavigation {

    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavHostController
}

