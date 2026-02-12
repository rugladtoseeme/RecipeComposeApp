package com.mycompany.recipecomposeapp.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

sealed class Quantity {
    data class Measured(val amount: Double, val unit: String) : Quantity()
    object ByTaste : Quantity()
}

data class IngredientDto(val name: String, val quantity: Quantity)

@Immutable
@Parcelize
data class IngredientUiModel(
    val title: String,
    val amount: String,
): Parcelable

fun IngredientDto.toUiModel() = IngredientUiModel(
    title = name,
    amount = when (quantity) {
        is Quantity.Measured -> "${quantity.amount} ${quantity.unit}"
        is Quantity.ByTaste -> "по вкусу"
    }
)