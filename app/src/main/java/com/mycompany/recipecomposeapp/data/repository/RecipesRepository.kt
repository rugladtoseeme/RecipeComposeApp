package com.mycompany.recipecomposeapp.data.repository

import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {

    fun getCategories(): Flow<List<CategoryDto>>

    fun getRecipesByCategoryId(categoryId: Int): Flow<List<RecipeDto>>

    fun getRecipe(recipeId: Int): Flow<RecipeDto?>
}