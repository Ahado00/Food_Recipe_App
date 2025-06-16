package com.example.foodrecipeapp.datasource.remote

data class Recipe (
    val pk: Int,
    val title: String,
    val publisher: String,
    val featured_image: String,
    val rating: Int,
    val source_url: String
)