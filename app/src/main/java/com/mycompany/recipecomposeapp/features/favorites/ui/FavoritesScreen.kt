package com.mycompany.recipecomposeapp.features.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mycompany.recipecomposeapp.core.ui.ScreenHeader
import com.mycompany.recipecomposeapp.features.favorites.presentation.FavoritesViewModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.mycompany.recipecomposeapp.features.recipes.ui.RecipeItem

@Composable
fun FavoritesScreen(
    drawableResId: Int,
    headerText: String,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int, RecipeUiModel) -> Unit
) {

    val viewModel: FavoritesViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            imageResId = drawableResId,
            title = headerText
        )

        if (uiState.favorites.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.favorites,
                    key = { recipe -> recipe.id }
                ) { recipe: RecipeUiModel ->
                    RecipeItem(recipe = recipe, onRecipeClick = onRecipeClick)
                }
            }

        } else {
            Spacer(Modifier.weight(weight = 1f))

            Text(
                text = "Вы еще не добавили ни одного рецепта в избранное",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                fontSize = 30.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(weight = 1f))
        }
    }
}
