package com.example.foodrecipeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodrecipeapp.components.RecipeCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodrecipeapp.ui.state.RecipesListUiState
import com.example.foodrecipeapp.viewmodels.RecipesListViewModel

/**
 * This is the Home Screen
 * it contains:
 * - Search bar
 * - Recipes List
 * */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen () {

    val viewModel : RecipesListViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecipes()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)
    ) {

        // Page title
        Text("Meals Recipes", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {viewModel.onSearchQueryChange(it)}, //Call search function
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            placeholder = { Text("Search meal name") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search"
                ) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFF5F5F5),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent

            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Handling states of showing the Recipes List
        when(state){

            is RecipesListUiState.Loading -> { // when list is loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.LightGray)
                }
            }

            is RecipesListUiState.Success -> { // when list returns successfully
                val recipes = (state as RecipesListUiState.Success).recipes
                val listState = rememberLazyGridState()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    state = listState
                ) {
                    items(recipes.size) { index ->
                        val recipe = recipes[index]
                        RecipeCard(
                            title = recipe.title,
                            publisher = recipe.publisher,
                            imageUrl = recipe.featured_image
                        )

                        if (index >= recipes.size - 4) {
                            LaunchedEffect(key1 = index) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                    if (isLoadingMore) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.Gray)
                            }
                        }
                    }
                }

            }

            is RecipesListUiState.Error -> { // when there is an error
                val message = (state as RecipesListUiState.Error).message
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}