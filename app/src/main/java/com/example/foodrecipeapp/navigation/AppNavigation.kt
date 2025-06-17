package com.example.foodrecipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodrecipeapp.screens.HomeScreen
import com.example.foodrecipeapp.screens.RecipeDetailScreen

@Composable
fun AppNavigation( navController : NavHostController){

    NavHost(
        navController = navController,
        startDestination = "home"){

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
            recipeId?.let {
                RecipeDetailScreen(recipeId = it, navController = navController)
            }
        }
    }

}
