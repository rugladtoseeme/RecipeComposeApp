package com.mycompany.recipecomposeapp.data.repository

import app.cash.turbine.test
import com.mycompany.recipecomposeapp.core.network.api.RecipesApiService
import com.mycompany.recipecomposeapp.data.database.RecipesDatabase
import com.mycompany.recipecomposeapp.data.database.dao.CategoryDao
import com.mycompany.recipecomposeapp.data.database.dao.RecipeDao
import com.mycompany.recipecomposeapp.data.database.entity.CategoryEntity
import com.mycompany.recipecomposeapp.data.database.entity.RecipeEntity
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RecipesRepositoryTest {

    private val apiService = mockk<RecipesApiService>()
    private val database = mockk<RecipesDatabase>(relaxed = true)
    private val categoryDao = mockk<CategoryDao>()
    private val recipeDao = mockk<RecipeDao>()

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        every { database.categoryDao() } returns categoryDao
        every { database.recipeDao() } returns recipeDao
        repository = RecipesRepositoryImpl(apiService, database)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    @Test
    fun `getCategories emits categories from database`() = runTest {
        every { categoryDao.getCategories() } returns flowOf(
            listOf(
                CategoryEntity(
                    id = 1,
                    name = "Завтраки",
                    description = "Утренние блюда",
                    imageUrl = "breakfast.jpg"
                )
            )
        )
        coEvery { apiService.getCategories() } returns emptyList()
        coEvery { categoryDao.insertCategories(any()) } just Runs

        repository.getCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals("Завтраки", categories[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCategories still emits data when api throws exception`() = runTest {
        every { categoryDao.getCategories() } returns flowOf(
            listOf(
                CategoryEntity(
                    id = 1,
                    name = "Завтраки",
                    description = "Утренние блюда",
                    imageUrl = "breakfast.jpg"
                )
            )
        )
        coEvery { apiService.getCategories() } throws IOException()
        coEvery { categoryDao.insertCategories(any()) } just Runs

        repository.getCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals("Завтраки", categories[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRecipesByCategory returns flow filtered by categoryId`() = runTest {
        every { recipeDao.getRecipesByCategoryId(0) } returns flowOf(
            listOf(
                RecipeEntity(
                    id = 1, title = "Панкейки",
                    imageUrl = "pancakes.jpg",
                    ingredients = "",
                    method = "",
                    categoryId = 0
                )
            )
        )
        coEvery { apiService.getCategories() } returns emptyList()
        coEvery { categoryDao.insertCategories(any()) } just Runs

        repository.getRecipesByCategoryId(0).test {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            assertEquals("Панкейки", recipes[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
