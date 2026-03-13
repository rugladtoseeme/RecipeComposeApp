package com.mycompany.recipecomposeapp.features.details.presentation.model

import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class RecipeDetailsUiState(
    val recipe: RecipeUiModel = RecipeUiModel(
        id = -1,
        title = "",
        imageUrl = "TODO()",
        ingredients = listOf(),
        method = listOf(),
        isFavorite = false,
        servings = 1
    ),
    val numberOfPortions: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
