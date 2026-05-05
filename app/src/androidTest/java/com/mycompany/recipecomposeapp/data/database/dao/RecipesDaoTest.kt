package com.mycompany.recipecomposeapp.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mycompany.recipecomposeapp.data.database.RecipesDatabase
import com.mycompany.recipecomposeapp.data.database.entity.CategoryEntity
import com.mycompany.recipecomposeapp.data.database.entity.RecipeEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesDaoTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()
        recipeDao = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertsAndRetrievesCategories() = runTest {
        val categories = listOf(
            CategoryEntity(id = 1, name = "Завтраки", description = "Лёгкие", imageUrl = ""),
            CategoryEntity(id = 2, name = "Обеды", description = "Основные", imageUrl = "")
        )

        categoryDao.insertCategories(categories)
        val retrieved = categoryDao.getCategories().first()

        assertEquals(2, retrieved.size)
    }

    @Test
    fun insertReplacesDuplicateCategory() = runTest {
        val categories = listOf(
            CategoryEntity(id = 1, name = "Завтраки", description = "Лёгкие", imageUrl = ""),
            CategoryEntity(id = 1, name = "Завтраки", description = "Лёгкие", imageUrl = "")
        )

        categoryDao.insertCategories(categories)
        val retrieved = categoryDao.getCategories().first()

        assertEquals(1, retrieved.size)
    }

    @Test
    fun getRecipesByCategoryReturnsCorrectItems() = runTest {

        val categories = listOf(
            CategoryEntity(id = 1, name = "Завтраки", description = "Лёгкие", imageUrl = ""),
            CategoryEntity(id = 2, name = "Обеды", description = "Основные", imageUrl = ""),
            CategoryEntity(id = 3, name = "Салаты", description = "Полезные", imageUrl = ""),
        )
        categoryDao.insertCategories(categories)

        val recipes = listOf(
            RecipeEntity(
                id = 2,
                categoryId = 2,
                title = "Борщ",
                imageUrl = "",
                ingredients = "",
                method = ""
            ),
            RecipeEntity(
                id = 4,
                categoryId = 1,
                title = "Гранола",
                imageUrl = "",
                ingredients = "",
                method = ""
            ),
            RecipeEntity(
                id = 5,
                categoryId = 1,
                title = "Чикен макмаффин",
                imageUrl = "",
                ingredients = "",
                method = ""
            ),
            RecipeEntity(
                id = 1,
                categoryId = 1,
                title = "Омлет",
                imageUrl = "",
                ingredients = "",
                method = ""
            ),
            RecipeEntity(
                id = 3,
                categoryId = 3,
                title = "Салат с тунцом",
                imageUrl = "",
                ingredients = "",
                method = ""
            ),
        )

        recipeDao.insertRecipes(recipes)
        val retrieved = recipeDao.getRecipesByCategoryId(1).first()

        retrieved.forEach { assertEquals(1, it.categoryId) }

    }

    @Test
    fun emptyDatabaseReturnsEmptyList() = runTest {
        val categories = emptyList<CategoryEntity>()

        categoryDao.insertCategories(categories)
        val retrieved = categoryDao.getCategories().first()

        assert(retrieved.isEmpty())
    }
}