package com.example.foodrecipeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipeapp.network.RecipeApiService
import com.example.foodrecipeapp.ui.state.RecipesDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * this VM handles the logic of fetching a specific recipe by ID
 **/
class RecipeDetailViewModel(private val api: RecipeApiService) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipesDetailsUiState>(RecipesDetailsUiState.Loading)
    val uiState: StateFlow<RecipesDetailsUiState> = _uiState

    fun getRecipeById(id: Int) {
        _uiState.value = RecipesDetailsUiState.Loading
        viewModelScope.launch {
            try {
                val recipe = api.getRecipeById(id)
                _uiState.value = RecipesDetailsUiState.Success(recipe)
            } catch (e: Exception) {
                _uiState.value = RecipesDetailsUiState.Error("Failed to load recipe: ${e.localizedMessage}")
            }
        }
    }
}