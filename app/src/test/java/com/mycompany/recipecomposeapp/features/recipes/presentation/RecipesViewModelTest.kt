package com.mycompany.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import fixtures.RecipeTestFixtures
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RecipesViewModelTest {

    private val repository = mockk<RecipesRepository>()
    private lateinit var viewModel: RecipesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loads recipes for category`() {
        every { repository.getRecipesByCategoryId(1) } returns flowOf(
            RecipeTestFixtures.createRecipeDtoList(
                3
            )
        )
        viewModel = RecipesViewModel(
            repository = repository,
            savedState = SavedStateHandle(
                mapOf(
                    "categoryId" to 1,
                    "categoryTitle" to "Завтраки",
                    "categoryImageUrl" to "breakfast.png"
                )
            )
        )

        assertEquals(viewModel.uiState.value.recipes.size, 3)

    }

    @Test
    fun `state reflects category title from savedState`() {

        val categoryTitle = "ЗАВТРАКИ"
        every { repository.getRecipesByCategoryId(1) } returns flowOf(
            RecipeTestFixtures.createRecipeDtoList(
                3
            )
        )
        viewModel = RecipesViewModel(
            repository = repository,
            savedState = SavedStateHandle(
                mapOf(
                    "categoryId" to 1,
                    "categoryTitle" to categoryTitle,
                    "categoryImageUrl" to "breakfast.png"
                )
            )
        )

        assertEquals(viewModel.uiState.value.categoryName, categoryTitle)
    }

    @Test
    fun `shows error when repository throws`() {
        every { repository.getRecipesByCategoryId(1) } throws IOException()
        viewModel = RecipesViewModel(
            repository = repository,
            savedState = SavedStateHandle(
                mapOf(
                    "categoryId" to 1,
                    "title" to "Завтраки",
                    "imageUrl" to "breakfast.png"
                )
            )
        )

        assert(viewModel.uiState.value.error != null)
    }
}