package com.mycompany.recipecomposeapp.features.recipes.presentation.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: List<IngredientUiModel>,
    val method: List<String>,
    val isFavorite: Boolean = false,
    val servings : Int = 1
): Parcelable