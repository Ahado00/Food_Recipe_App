package com.example.foodrecipeapp.ui.state

import com.example.foodrecipeapp.datasource.remote.Recipe
import com.example.foodrecipeapp.datasource.remote.RecipeDetail

sealed class RecipesListUiState {
    object Loading : RecipesListUiState()
    data class Success (val recipes: List<Recipe>) : RecipesListUiState()
    data class DetailSuccess (val recipe: RecipeDetail) : RecipesListUiState()
    data class Error (val message : String) : RecipesListUiState()
}