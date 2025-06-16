package com.example.foodrecipeapp.network

import com.example.foodrecipeapp.datasource.remote.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipeApiService {
    @Headers("Authorization: Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
    @GET("api/recipe/search")
    suspend fun searchRecipes(
        @Query("page") page: Int = 1,
        @Query("query") query: String = ""
    ): RecipeResponse

}