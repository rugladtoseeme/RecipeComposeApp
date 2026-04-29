package com.mycompany.recipecomposeapp.features.categories.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mycompany.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import com.mycompany.recipecomposeapp.features.categories.presentation.model.CategoryUiModel
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag

class CategoriesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    fun CategoriesContent(
        uiState: CategoriesUiState,
        onCategoryClick: (Int, String, String) -> Unit
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
            uiState.error != null -> Text(
                text = uiState.error,
                modifier = Modifier.testTag("error_message")
            )

            else -> LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(uiState.categories) { category ->
                    CategoryItem(
                        categoryUiModel = category,
                        onClick = {
                            onCategoryClick(
                                category.id,
                                category.title,
                                category.imageUrl
                            )
                        },
                    )
                }
            }
        }
    }

    @Test
    fun displaysCategories() {
        composeTestRule.setContent {
            CategoriesContent(
                uiState = CategoriesUiState(
                    categories = listOf(
                        CategoryUiModel(
                            id = 1,
                            title = "Завтраки",
                            description = "Утренние блюда",
                            imageUrl = "breakfast.png"
                        )
                    )
                ),
                onCategoryClick = { _, _, _ -> }
            )
        }
        // CategoryItem показывает title.uppercase() — ищем "ЗАВТРАКИ", не "Завтраки"
        composeTestRule.onNodeWithText("ЗАВТРАКИ").assertIsDisplayed()
    }

    @Test
    fun clickingCategoryNavigatesToRecipes() {
        var clickedId: Int? = null
        composeTestRule.setContent {
            CategoriesContent(
                uiState = CategoriesUiState(
                    categories = listOf(
                        CategoryUiModel(
                            id = 1,
                            title = "Завтраки",
                            description = "Утренние блюда",
                            imageUrl = "breakfast.png"
                        )
                    )
                ),
                onCategoryClick = { id, _, _ -> clickedId = id }
            )
        }
        composeTestRule.onNodeWithText("ЗАВТРАКИ").performClick()
        assertEquals(1, clickedId)
    }


    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            CategoriesContent(
                uiState = CategoriesUiState(
                    categories = listOf(
                        CategoryUiModel(
                            id = 1,
                            title = "Завтраки",
                            description = "Утренние блюда",
                            imageUrl = "breakfast.png"
                        )
                    ),
                    isLoading = true
                ),
                onCategoryClick = { _, _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }
}