package com.mycompany.recipecomposeapp.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.network.api.RecipesApiService
import com.mycompany.recipecomposeapp.data.database.RecipesDatabase
import com.mycompany.recipecomposeapp.data.database.dao.CategoryDao
import com.mycompany.recipecomposeapp.data.database.entity.CategoryEntity
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RecipesRepositoryIntegrationTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private val apiService = mockk<RecipesApiService>()

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        categoryDao = database.categoryDao()
        repository = RecipesRepositoryImpl(apiService = apiService, database = database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savesDataToCacheAfterSuccessfulApiCall() = runTest {
        coEvery { apiService.getCategories() } returns listOf(
            CategoryDto(
                id = 1,
                title = "Завтраки",
                description = "Лёгкие",
                imageUrl = "breakfast.jpg"
            )
        )

        repository.getCategories().test {
            awaitItem()  // пустой список из БД (кеш пуст)
            val loaded = awaitItem()  // список после загрузки из API
            assertEquals("Завтраки", loaded.first().title)
            cancelAndIgnoreRemainingEvents()
        }

        val cached = categoryDao.getCategories().first()
        assertEquals(1, cached.size)  // данные сохранились в Room
        assertEquals("Завтраки", cached[0].name)
    }

    @Test
    fun returnsCachedDataWhenApiFails() = runTest {

        categoryDao.insertCategories(
            listOf(
                CategoryEntity(
                    id = 1,
                    name = "Завтраки",
                    description = "Лёгкие",
                    imageUrl = "breakfast.jpg"
                )
            )
        )
        coEvery { apiService.getCategories() } throws IOException()

        val cached = categoryDao.getCategories().first()
        assertEquals(1, cached.size)  // данные сохранились в Room
        assertEquals("Завтраки", cached[0].name)
    }
}
