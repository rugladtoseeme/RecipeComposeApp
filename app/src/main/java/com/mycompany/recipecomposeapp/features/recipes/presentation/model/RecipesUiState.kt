package com.mycompany.recipecomposeapp.features.recipes.presentation.model

data class RecipesUiState(
    val recipes: List<RecipeUiModel> = emptyList(),
    val categoryName: String,
    val categoryImageUrl: String,
    val isLoading: Boolean = false,
    val error: String? = null,
    val emptyRecipeList: Boolean = recipes.isEmpty()
)


