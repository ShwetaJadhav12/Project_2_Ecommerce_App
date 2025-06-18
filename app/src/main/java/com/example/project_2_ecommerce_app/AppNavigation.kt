package com.example.project_2_ecommerce_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_2_ecommerce_app.screen.AuthScreen
import com.example.project_2_ecommerce_app.screen.HomeScreen
import com.example.project_2_ecommerce_app.screen.LoginScreen
import com.example.project_2_ecommerce_app.screen.SignupScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = "auth"
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
            HomeScreen()
        }

    }
}
