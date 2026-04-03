package com.mycompany.recipecomposeapp.core.model

import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.mycompany.recipecomposeapp.core.ui.Constants
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    @Contextual
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String
)

fun RecipeDto.toUiModel() = RecipeUiModel(
    id = id,
    title = title,
    imageUrl = if (imageUrl.startsWith("http")) imageUrl else Constants.IMAGES_BASE_URL + imageUrl,
    ingredients = ingredients.map { it.toUiModel() },
    method = method,
    isFavorite = false
)