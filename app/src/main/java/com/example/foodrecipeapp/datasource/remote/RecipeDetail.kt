package com.example.foodrecipeapp.datasource.remote

data class RecipeDetail(
    val pk: Int,
    val title: String,
    val publisher: String,
    val featured_image: String,
    val source_url: String,
    val ingredients: List<String>,
)
