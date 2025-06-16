package com.example.foodrecipeapp.ui.state

import com.example.foodrecipeapp.datasource.remote.Recipe

sealed class RecipesListUiState {
    object Loading : RecipesListUiState()
    data class Success (val recipes: List<Recipe>) : RecipesListUiState()
    data class Error (val message : String) : RecipesListUiState()
}