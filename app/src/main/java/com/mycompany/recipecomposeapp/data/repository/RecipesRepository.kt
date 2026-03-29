package com.mycompany.recipecomposeapp.data.repository

import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.model.RecipeDto

interface RecipesRepository {

    suspend fun getCategories(): List<CategoryDto>

    suspend fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto>

    suspend fun getRecipe(recipeId: Int): RecipeDto?
}