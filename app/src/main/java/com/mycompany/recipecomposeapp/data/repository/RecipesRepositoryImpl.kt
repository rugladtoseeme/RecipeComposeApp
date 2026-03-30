package com.mycompany.recipecomposeapp.data.repository

import android.util.Log
import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import com.mycompany.recipecomposeapp.core.network.api.RecipesApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepositoryImpl(private val apiService: RecipesApiService) : RecipesRepository {

    override suspend fun getCategories(): List<CategoryDto> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getCategories()
            } catch (e: Exception) {
                Log.e(
                    "RecipesRepositoryImpl",
                    "Не удалось получить категории",
                    e
                )
                emptyList()
            }
        }
    }

    override suspend fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getRecipesByCategory(categoryId)
            } catch (e: Exception) {
                Log.e(
                    "RecipesRepositoryImpl",
                    "Не удалось получить рецепты из категории $categoryId",
                    e
                )
                emptyList()
            }
        }
    }

    override suspend fun getRecipe(recipeId: Int): RecipeDto? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getRecipe(recipeId)
            } catch (e: Exception) {
                Log.e(
                    "RecipesRepositoryImpl",
                    "Не удалось получить рецепт по id $recipeId",
                    e
                )
                throw e
            }
        }
    }
}