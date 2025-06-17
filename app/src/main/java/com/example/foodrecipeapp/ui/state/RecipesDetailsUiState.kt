package com.example.foodrecipeapp.ui.state

import com.example.foodrecipeapp.datasource.remote.Recipe
import com.example.foodrecipeapp.datasource.remote.RecipeDetail

sealed class RecipesDetailsUiState {
    object Loading : RecipesDetailsUiState()
    data class Success (val recipe: RecipeDetail) : RecipesDetailsUiState()
    data class Error (val message : String) : RecipesDetailsUiState()
}