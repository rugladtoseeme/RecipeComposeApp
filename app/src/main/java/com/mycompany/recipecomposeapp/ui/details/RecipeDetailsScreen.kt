package com.mycompany.recipecomposeapp.ui.details

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.R
import com.mycompany.recipecomposeapp.data.model.IngredientUiModel
import com.mycompany.recipecomposeapp.data.model.Quantity
import com.mycompany.recipecomposeapp.data.model.RecipeUiModel
import com.mycompany.recipecomposeapp.ui.core.ui.ScreenHeader
import com.mycompany.recipecomposeapp.ui.core.ui.shareRecipe
import kotlin.math.roundToInt

@Composable
fun RecipeHeader(
    recipe: RecipeUiModel?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShareClick: () -> Unit,
    showShareButton: Boolean = true,
    showFavoriteButton: Boolean = true,
) {

    Box(modifier = Modifier.fillMaxWidth()) {
        ScreenHeader(
            imageUrl = recipe?.imageUrl,
            title = recipe?.title ?: "Рецепт"
        )

        if (showFavoriteButton) {
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(40.dp),
                enabled = true
            )
            {

                Crossfade(
                    targetState = isFavorite,
                    animationSpec = tween(durationMillis = 300),
                    label = "favorite_animation"
                ) { isCurrentlyFavorite ->
                    val heartIcon = rememberVectorPainter(
                        image = ImageVector.vectorResource(
                            id = if (isCurrentlyFavorite) R.drawable.ic_heart_active else R.drawable.ic_heart_empty
                        )
                    )

                    Icon(
                        painter = heartIcon,
                        contentDescription = "Favorite",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.Unspecified
                    )
                }
            }
        }

        if (showShareButton) {
            IconButton(
                onClick = onShareClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 60.dp)
                    .wrapContentSize(),
                enabled = true
            )
            {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "share button image",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Unspecified
                )
            }
        }
    }
}

private fun adjustIngredient(ingredient: IngredientUiModel, multiplier: Float): IngredientUiModel =
    ingredient.copy(
        quantity = if (ingredient.quantity is Quantity.Measured)
            ingredient.quantity.copy(amount = ingredient.quantity.amount * multiplier)
        else ingredient.quantity
    )

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

//    var isFavorite by remember(recipe?.id) {
//        mutableStateOf(favoritePrefs.isFavorite(recipe?.id))
//    }

    var currentPortions by rememberSaveable { mutableIntStateOf(recipe?.servings ?: 1) }

    val adjustedIngredients = remember(recipe?.ingredients, currentPortions) {
        val multiplier = currentPortions.toFloat() / (recipe?.servings?.toFloat() ?: 1.0f)
        recipe?.ingredients?.map { ingredient ->
            adjustIngredient(ingredient, multiplier)
        }
    } ?: listOf()

    Column(modifier = modifier.fillMaxWidth()) {

        RecipeHeader(
            recipe = recipe,
            isFavorite = isFavorite,
            onToggleFavorite = onToggleFavorite,
            showFavoriteButton = true,
            onShareClick = {
                shareRecipe(context, recipe?.id ?: -1, recipe?.title)
            }
        )

        LazyColumn(modifier = Modifier.padding(16.dp)) {

            item { PortionsSelector(currentPortions, { portions -> currentPortions = portions }) }

            items(items = adjustedIngredients) { ingredient: IngredientUiModel ->
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
                text = "${index + 1}.$step",
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