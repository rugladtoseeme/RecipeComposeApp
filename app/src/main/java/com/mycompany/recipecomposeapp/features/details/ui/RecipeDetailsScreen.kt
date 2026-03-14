package com.mycompany.recipecomposeapp.features.details.ui

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mycompany.recipecomposeapp.R
import com.mycompany.recipecomposeapp.core.ui.ScreenHeader
import com.mycompany.recipecomposeapp.core.ui.shareRecipe
import com.mycompany.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
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

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel?,
    modifier: Modifier = Modifier
) {

    val viewModel: RecipeDetailsViewModel = viewModel()

    LaunchedEffect(recipe) {
        recipe?.let { viewModel.initializeWithRecipe(it) }
    }

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val adjustedIngredients = remember(uiState.recipe.ingredients, uiState.numberOfPortions) {
        viewModel.adjustIngredients()
    }

    Column(modifier = modifier.fillMaxWidth()) {

        RecipeHeader(
            recipe = uiState.recipe,
            isFavorite = uiState.isFavorite,
            onToggleFavorite = { viewModel.toggleFavorite() },
            showFavoriteButton = true,
            onShareClick = {
                shareRecipe(context, uiState.recipe.id, uiState.recipe.title)
            }
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        } else if (uiState.error != null) {
            Spacer(Modifier.weight(1f))
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.weight(1f))
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {

                item {
                    PortionsSelector(uiState.numberOfPortions, { portions ->
                        viewModel.updatePortions(portions)
                    })
                }

                items(items = adjustedIngredients) { ingredient: IngredientUiModel ->
                    IngredientItem(
                        ingredient = ingredient,
                        modifier = Modifier.padding(start = 12.dp)
                    )
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
                item { InstructionsList(uiState.recipe.method) }
            }
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