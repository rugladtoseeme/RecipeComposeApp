package com.mycompany.recipecomposeapp.features.recipes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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