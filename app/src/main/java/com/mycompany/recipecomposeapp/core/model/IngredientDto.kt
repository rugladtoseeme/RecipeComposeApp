package com.mycompany.recipecomposeapp.core.model

import android.os.Parcelable
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt


@Parcelize
sealed class Quantity : Parcelable {

    data class Measured(val amount: Double, val unit: String) : Quantity() {
        override fun toString() = "${
            if (amount % 1.0 <= 0.1 || amount % 1.0 >= 0.9) {
                amount.roundToInt().toString()
            } else {
                amount.toString() 
            }
        } ${unit}"
    }

    object ByTaste : Quantity() {
        override fun toString() = "по вкусу"
    }
}

data class IngredientDto(val name: String, val quantity: Quantity)

fun IngredientDto.toUiModel() = IngredientUiModel(
    title = name,
    quantity = quantity
)