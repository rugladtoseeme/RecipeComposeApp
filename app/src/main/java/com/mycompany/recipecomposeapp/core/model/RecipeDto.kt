package com.mycompany.recipecomposeapp.core.model

import com.mycompany.recipecomposeapp.features.recipes.presentation.RecipeUiModel
import com.mycompany.recipecomposeapp.core.ui.Constants

data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String
)


fun RecipeDto.toUiModel() = RecipeUiModel(
    id = id,
    title = title,
    imageUrl = if (imageUrl.startsWith("http")) imageUrl else Constants.ASSETS_URI_PREFIX + imageUrl,
    ingredients = ingredients.map { it.toUiModel() },
    method = method,
    isFavorite = false
)

