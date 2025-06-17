package com.example.foodrecipeapp.ui.state

import com.example.foodrecipeapp.datasource.remote.RecipeDetail
/**
 * This sealed class holds the UI states of Recipe Details Screen
 * */
sealed class RecipesDetailsUiState {
    object Loading : RecipesDetailsUiState()
    data class Success (val recipe: RecipeDetail) : RecipesDetailsUiState()
    data class Error (val message : String) : RecipesDetailsUiState()
}