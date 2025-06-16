package com.example.foodrecipeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipeapp.datasource.remote.Recipe
import com.example.foodrecipeapp.network.RetrofitInstance
import com.example.foodrecipeapp.ui.state.RecipesListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipesListViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _uiState = MutableStateFlow<RecipesListUiState>(RecipesListUiState.Loading)
    val uiState: StateFlow<RecipesListUiState> = _uiState

    fun getRecipes(query: String = "") {
        viewModelScope.launch {
            _uiState.value = RecipesListUiState.Loading
            try {
                val response = RetrofitInstance.api.searchRecipes(query = query)
                if (response.results.isNotEmpty()) {
                    _uiState.value = RecipesListUiState.Success(response.results)
                } else {
                    _uiState.value = RecipesListUiState.Error("No recipes found.")
                }
            } catch (e: Exception) {
                _uiState.value = RecipesListUiState.Error("Failed to load recipes.")
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        getRecipes(newQuery)
    }
}
