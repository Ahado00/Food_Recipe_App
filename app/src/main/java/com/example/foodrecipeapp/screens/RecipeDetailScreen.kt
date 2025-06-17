package com.example.foodrecipeapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.foodrecipeapp.network.RetrofitInstance
import com.example.foodrecipeapp.ui.state.RecipesDetailsUiState
import com.example.foodrecipeapp.ui.state.RecipesListUiState
import com.example.foodrecipeapp.viewmodels.RecipeDetailViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextDecoration

/**
 * This is the Recipe Details Screen that is
 * showing the info of specific recipe
 * it contains:
 * - Full title of the recipe
 * - The publisher
 * - Ingredients list
 * - Recipe URL
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(recipeId: Int , navController : NavHostController){

    val viewModel = remember {RecipeDetailViewModel(RetrofitInstance.api)}
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.getRecipeById(recipeId)
    }

    when (state) {
        is RecipesDetailsUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.LightGray)
        }
        is RecipesDetailsUiState.Success -> {
            val recipe = (state as RecipesDetailsUiState.Success).recipe

            Column( modifier = Modifier
                .fillMaxSize()) {
                TopAppBar(
                    title = { Text("Recipe Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(recipe.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    Text("Published by: ${recipe.publisher}", fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(Modifier.height(8.dp))
                    AsyncImage(
                        model = recipe.featured_image,
                        contentDescription = recipe.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(8.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Ingredients:", fontSize = 18.sp ,fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    recipe.ingredients.forEach {
                        Text("- $it", fontSize = 14.sp)
                        Spacer(Modifier.height(4.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Recipe URL:", fontSize = 18.sp ,fontWeight = FontWeight.Bold)
                    RecipeUrlText(recipe.source_url)
                }
            }
        }

        is RecipesDetailsUiState.Error -> {
            val message = (state as RecipesListUiState.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message, color = Color.Red)
            }
        }
    }
}

/**
 * This composable function is to create a clickable URL
 * Used above in Recipe URL
 * */
@Composable
fun RecipeUrlText(url: String) {
    val context = LocalContext.current

    Text(
        text = " $url",
        fontSize = 14.sp,
        color = Color.Blue,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}