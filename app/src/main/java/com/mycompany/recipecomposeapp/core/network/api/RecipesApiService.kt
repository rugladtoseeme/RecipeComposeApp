package com.mycompany.recipecomposeapp.core.network.api

import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApiService {

    @GET("category")
    suspend fun getCategories(): List<CategoryDto>

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategory(@Path("id") categoryId: Int): List<RecipeDto>

    @GET("recipe/{id}")
    suspend fun getRecipe(@Path("id") recipeId: Int): RecipeDto?

    @GET("recipes/")
    suspend fun getRecipesByIdsString(@Query("ids") recipeIdsStr: String): List<RecipeDto>

}