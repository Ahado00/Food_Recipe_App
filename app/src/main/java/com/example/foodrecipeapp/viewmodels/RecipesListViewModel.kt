package com.example.foodrecipeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipeapp.datasource.remote.Recipe
import com.example.foodrecipeapp.network.RetrofitInstance
import com.example.foodrecipeapp.ui.state.RecipesListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
/**
 * this VM handles the logic of fetching recipe list
 * also handles the search meal by name along with "load more" state
 * */
class RecipesListViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _uiState = MutableStateFlow<RecipesListUiState>(RecipesListUiState.Loading)
    val uiState: StateFlow<RecipesListUiState> = _uiState

    private var currentPage = 1
    private var endReached = false
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore
    private var currentRecipes = mutableListOf<Recipe>()


    fun getRecipes(query: String = "", page: Int = 1) {

        if (_isLoadingMore.value || endReached) return
        _isLoadingMore.value = true

        viewModelScope.launch {
            if (page == 1) {
                _uiState.value = RecipesListUiState.Loading
                currentRecipes.clear()
                endReached = false
            }
            try {
                val response = RetrofitInstance.api.searchRecipes(query = query, page = page)
                val newRecipes = response.results.filter { it.title.contains(query, ignoreCase = true) }

                if (newRecipes.isEmpty()) {
                    endReached = true
                }

                currentRecipes.addAll(newRecipes)
                _uiState.value = RecipesListUiState.Success(currentRecipes)
                currentPage = page

            } catch (e: Exception) {
                _uiState.value = RecipesListUiState.Error("Failed to load recipes.")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        getRecipes(newQuery)
    }


    fun loadNextPage() {
        if (!_isLoadingMore.value && !endReached) {
            getRecipes(query = _searchQuery.value, page = currentPage + 1)
        }
    }
}
