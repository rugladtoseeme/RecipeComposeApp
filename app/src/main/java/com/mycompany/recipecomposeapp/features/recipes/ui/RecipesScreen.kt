package com.mycompany.recipecomposeapp.features.recipes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.core.ui.ScreenHeader
import com.mycompany.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipesUiState

@Composable
fun RecipesScreen(
    modifier: Modifier = Modifier,
    onRecipeClick: (Int, RecipeUiModel) -> Unit,
    viewModel: RecipesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            imageUrl = uiState.categoryImageUrl,
            uiState.categoryName
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
        } else if (uiState.emptyRecipeList) {
            Spacer(Modifier.weight(1f))
            Text(
                text = "Нет рецептов в данной категории",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(items = uiState.recipes, key = { it.id }) { recipe ->
                    RecipeItem(recipe = recipe, onRecipeClick = onRecipeClick)
                }
            }
        }
    }
}

@Composable
fun RecipesContent(
    uiState: RecipesUiState,
    onRecipeClick: (Int, RecipeUiModel) -> Unit
) {
    when {
        uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
        uiState.error != null -> Text(
            text = uiState.error,
            modifier = Modifier.testTag("error_message")
        )

        uiState.emptyRecipeList -> Text(
            text = "В данной категории нет ни одного рецепта",
            modifier = Modifier.testTag("empty_state")
        )

        else -> LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(uiState.recipes) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onRecipeClick = onRecipeClick,
                    modifier = Modifier.testTag("Карбонара")
                )
            }
        }
    }
}
