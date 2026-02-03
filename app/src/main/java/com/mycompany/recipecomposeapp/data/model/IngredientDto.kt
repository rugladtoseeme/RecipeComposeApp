package com.mycompany.recipecomposeapp.data.model

sealed class Quantity {
    data class Measured(val amount: Double, val unit: String): Quantity()
    object ByTaste: Quantity()
}
data class Ingredient(val name: String, val quantity: Quantity)

data class IngredientDto(val quantity: Quantity, val description: String)
