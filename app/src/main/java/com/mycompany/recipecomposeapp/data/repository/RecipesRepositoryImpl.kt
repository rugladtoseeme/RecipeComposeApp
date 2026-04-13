package com.mycompany.recipecomposeapp.data.repository

import android.util.Log
import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import com.mycompany.recipecomposeapp.core.model.toEntity
import com.mycompany.recipecomposeapp.core.network.api.RecipesApiService
import com.mycompany.recipecomposeapp.data.database.RecipesDatabase
import com.mycompany.recipecomposeapp.data.database.entity.toDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipesRepositoryImpl(
    private val apiService: RecipesApiService,
    database: RecipesDatabase
) : RecipesRepository {

    private val recipeDao = database.recipeDao()
    private val categoryDao = database.categoryDao()

    override fun getCategories(): Flow<List<CategoryDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fresh = apiService.getCategories()
                categoryDao.insertCategories(fresh.map { it.toEntity() })
                Log.d("RecipesRepositoryImpl", "Обновлено ${fresh.size} категорий")
            } catch (e: Exception) {
                Log.e("RecipesRepositoryImpl", "Ошибка обновления: ${e.message}", e)
            }
        }
        return categoryDao.getCategories().map { list -> list.map { it.toDto() } }
    }

    override fun getRecipesByCategoryId(categoryId: Int): Flow<List<RecipeDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fresh = apiService.getRecipesByCategory(categoryId)
                recipeDao.insertRecipes(fresh.map { it.toEntity(categoryId) })
                Log.d("RecipesRepositoryImpl", "Обновлено ${fresh.size} рецептов")
            } catch (e: Exception) {
                Log.e(
                    "RecipesRepositoryImpl",
                    "Не удалось получить рецепты из категории $categoryId",
                    e
                )
            }
        }
        return recipeDao.getRecipesByCategoryId(categoryId).map { list -> list.map { it.toDto() } }
    }

    override fun getRecipe(recipeId: Int): Flow<RecipeDto?>{
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fresh = apiService.getRecipe(recipeId)

                Log.d("RecipesRepositoryImpl", "Детали рецепта получены из API")
            } catch (e: Exception) {
                Log.e(
                    "RecipesRepositoryImpl",
                    "Не удалось получить рецепт по id $recipeId",
                    e
                )
            }
        }
        return recipeDao.getRecipeById(recipeId)
            .map { entity -> entity?.toDto()}
    }
}