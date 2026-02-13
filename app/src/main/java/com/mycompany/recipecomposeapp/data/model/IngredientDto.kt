package com.mycompany.recipecomposeapp.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
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

@Immutable
@Parcelize
data class IngredientUiModel(
    val title: String,
    val quantity: Quantity,
) : Parcelable

fun IngredientDto.toUiModel() = IngredientUiModel(
    title = name,
    quantity = quantity
)