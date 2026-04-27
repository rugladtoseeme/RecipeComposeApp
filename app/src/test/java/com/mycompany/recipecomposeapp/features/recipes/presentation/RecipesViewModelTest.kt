package com.mycompany.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import fixtures.RecipeTestFixtures
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {

    private val repository = mockk<RecipesRepository>()
    private lateinit var viewModel: RecipesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    private fun createViewModel(categoryId: Int, categoryTitle: String, categoryImageUrl: String) =
        RecipesViewModel(
            savedState = SavedStateHandle(
                mapOf(
                    "categoryId" to categoryId,
                    "categoryTitle" to categoryTitle,
                    "categoryImageUrl" to categoryImageUrl
                )
            ),
            repository = repository
        )

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loads recipes for category`() = runTest {
        every { repository.getRecipesByCategoryId(1) } returns flowOf(
            RecipeTestFixtures.createRecipeDtoList(
                3
            )
        )

        viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = "Завтраки",
            categoryImageUrl = "breakfast.png"
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(
                state.recipes.size,
                3
            )
            assertEquals(state.isLoading, false)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state reflects category title from savedState`() {

        val categoryTitle = "ЗАВТРАКИ"
        every { repository.getRecipesByCategoryId(1) } returns flowOf(
            RecipeTestFixtures.createRecipeDtoList(
                3
            )
        )

        viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = "Завтраки",
            categoryImageUrl = "breakfast.png"
        )

        assertEquals(viewModel.uiState.value.categoryName, categoryTitle)
    }

    @Test
    fun `shows error when repository throws`() {
        every { repository.getRecipesByCategoryId(1) } throws IOException()

        viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = "Завтраки",
            categoryImageUrl = "breakfast.png"
        )

        assert(viewModel.uiState.value.error != null)
    }
}