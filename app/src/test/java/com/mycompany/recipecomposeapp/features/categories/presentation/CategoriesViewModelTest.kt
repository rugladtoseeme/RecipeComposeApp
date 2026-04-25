package com.mycompany.recipecomposeapp.features.categories.presentation

import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import fixtures.CategoryTestFixtures
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    private val repository = mockk<RecipesRepository>()
    private lateinit var viewModel: CategoriesViewModel

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
    fun `loads categories from repository`() {
        every { repository.getCategories() } returns flowOf(CategoryTestFixtures.createCategoryDtoList(3))
        viewModel = CategoriesViewModel(repository)

        assertEquals(viewModel.uiState.value.categories.size, 3)
        assertEquals(viewModel.uiState.value.isLoading, false)

    }

    @Test
    fun `shows empty list when repository returns no data`() {
        every { repository.getCategories() } returns flowOf(emptyList())
        viewModel = CategoriesViewModel(repository)

        assert(viewModel.uiState.value.categories.isEmpty())
        assert(viewModel.uiState.value.error == null)
    }

    @Test
    fun `shows error when repository throws`() {

        every { repository.getCategories() } throws IOException()

        viewModel = CategoriesViewModel(repository)

        assert(viewModel.uiState.value.error != null)

    }
}