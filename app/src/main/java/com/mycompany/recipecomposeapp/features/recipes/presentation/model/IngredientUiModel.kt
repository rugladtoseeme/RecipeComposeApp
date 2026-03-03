package com.mycompany.recipecomposeapp.features.recipes.presentation.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.mycompany.recipecomposeapp.core.model.Quantity
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class IngredientUiModel(
    val title: String,
    val quantity: Quantity,
) : Parcelable