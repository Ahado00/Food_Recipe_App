package com.example.foodrecipeapp.ui.state

import com.example.foodrecipeapp.datasource.remote.Recipe
import com.example.foodrecipeapp.datasource.remote.RecipeDetail
/**
 * This sealed class holds the UI states of Home Screen
 * */
sealed class RecipesListUiState {
    object Loading : RecipesListUiState()
    data class Success (val recipes: List<Recipe>) : RecipesListUiState()
    data class Error (val message : String) : RecipesListUiState()
}