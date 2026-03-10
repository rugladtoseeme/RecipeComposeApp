package com.mycompany.recipecomposeapp.features.recipes.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mycompany.recipecomposeapp.core.ui.ScreenHeader
import com.mycompany.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun RecipesScreen(
    modifier: Modifier = Modifier,
    onRecipeClick: (Int, RecipeUiModel) -> Unit
) {

    val viewModel: RecipesViewModel = viewModel()
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
