package com.mycompany.recipecomposeapp.data.model

import androidx.compose.runtime.Immutable

sealed class Quantity {
    data class Measured(val amount: Double, val unit: String) : Quantity()
    object ByTaste : Quantity()
}

data class IngredientDto(val name: String, val quantity: Quantity)

@Immutable
data class IngredientUiModel(
    val title: String,
    val amount: String,
)

fun IngredientDto.toUiModel() = IngredientUiModel(
    title = name,
    amount = when (quantity) {
        is Quantity.Measured -> "${quantity.amount} ${quantity.unit}"
        is Quantity.ByTaste -> "по вкусу"
    }
)