package com.mycompany.recipecomposeapp.features.recipes.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import org.junit.Rule
import org.junit.Test

class RecipesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    fun RecipesContent(
        uiState: RecipesUiState,
        onRecipeClick: (Int, RecipeUiModel) -> Unit
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
            uiState.error != null -> Text(
                text = uiState.error,
                modifier = Modifier.testTag("error_message")
            )

            uiState.emptyRecipeList -> Text(
                text = "В данной категории нет ни одного рецепта",
                modifier = Modifier.testTag("empty_state")
            )

            else -> LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(uiState.recipes) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onRecipeClick = onRecipeClick,
                        modifier = Modifier.testTag("Карбонара")
                    )
                }
            }
        }
    }

    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = listOf(
                        RecipeUiModel(
                            id = 1,
                            title = "Карбонара",
                            imageUrl = "",
                            ingredients = listOf(),
                            method = listOf(),
                            isFavorite = false,
                            servings = 1,
                        )
                    ),
                    isLoading = true,
                    categoryName = "Паста",
                    categoryImageUrl = "pasta.png",
                ),
                onRecipeClick = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun showsErrorState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = listOf(
                        RecipeUiModel(
                            id = 1,
                            title = "Карбонара",
                            imageUrl = "",
                            ingredients = listOf(),
                            method = listOf(),
                            isFavorite = false,
                            servings = 1,
                        )
                    ),
                    isLoading = false,
                    categoryName = "Паста",
                    categoryImageUrl = "pasta.png",
                    error = "Network error",
                ),
                onRecipeClick = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
    }

    @Test
    fun showsEmptyState() {

        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = emptyList(),
                    isLoading = false,
                    categoryName = "",
                    categoryImageUrl = "",
                ),
                onRecipeClick = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
    }

    @Test
    fun displaysRecipeList() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = listOf(
                        RecipeUiModel(
                            id = 1,
                            title = "Карбонара",
                            imageUrl = "",
                            ingredients = listOf(),
                            method = listOf(),
                            isFavorite = false,
                            servings = 1,
                        )
                    ),
                    isLoading = false,
                    categoryName = "Паста",
                    categoryImageUrl = "pasta.png",
                ),
                onRecipeClick = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("Карбонара").performClick()
        composeTestRule.onNodeWithText("КАРБОНАРА").assertIsDisplayed()
    }
}