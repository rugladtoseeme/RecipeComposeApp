package com.mycompany.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.R
import com.mycompany.recipecomposeapp.data.model.IngredientUiModel
import com.mycompany.recipecomposeapp.data.model.Quantity
import com.mycompany.recipecomposeapp.data.model.RecipeUiModel
import com.mycompany.recipecomposeapp.ui.core.ui.ScreenHeader
import kotlin.math.roundToInt

@Composable
fun RecipeDetailsScreen(recipe: RecipeUiModel?, modifier: Modifier = Modifier) {

    var currentPortions by remember { mutableIntStateOf(recipe?.servings ?: 1) }

    val scaledIngredients = remember(currentPortions) {
        val multiplier = currentPortions.toDouble() / (recipe?.servings ?: 1)
        recipe?.ingredients?.map { ingredient ->
            ingredient.copy(
                quantity = if (ingredient.quantity is Quantity.Measured)
                    ingredient.quantity.copy(amount = ingredient.quantity.amount * multiplier)
                else ingredient.quantity
            )
        } ?: listOf()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            imageUrl = recipe?.imageUrl,
            title = recipe?.title ?: "РЕЦЕПТ",
        )

        LazyColumn(modifier = Modifier.padding(16.dp)) {

            item { PortionsSelector(currentPortions, { portions -> currentPortions = portions }) }

            items(items = scaledIngredients) { ingredient ->
                IngredientItem(ingredient = ingredient, modifier = Modifier.padding(start = 12.dp))
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = "СПОСОБ ПРИГОТОВЛЕНИЯ",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            item { InstructionsList(recipe?.method) }
        }
    }
}

@Composable
fun InstructionsList(method: List<String>?) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        method?.forEachIndexed { index, step ->
            Text(
                text = "${index+1}.$step",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun IngredientItem(ingredient: IngredientUiModel, modifier: Modifier = Modifier) {

    Row(
        modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {

        Text(
            text = ingredient.title.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(175.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = ingredient.quantity.toString().uppercase(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
private fun PortionsSlider(
    currentPortions: Int = 1,
    onPortionsChange: (Int) -> Unit
) {
    Slider(
        value = currentPortions.toFloat(),
        onValueChange = { onPortionsChange(it.roundToInt()) },
        valueRange = 1f..12f,
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
            inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
            thumbColor = MaterialTheme.colorScheme.tertiary
        )
    )
}

@Composable
fun PortionsSelector(currentPortions: Int, onPortionsChange: (Int) -> Unit) {

    Column {
        Text(
            text = "ИНГРЕДИЕНТЫ",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 6.dp)
        )

        Text(
            text = pluralStringResource(
                R.plurals.portions_count,
                currentPortions,
                currentPortions
            ),
            modifier = Modifier.padding(top = 6.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        PortionsSlider(currentPortions, onPortionsChange)
    }
}