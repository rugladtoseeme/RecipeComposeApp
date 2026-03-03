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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import com.mycompany.recipecomposeapp.features.recipes.presentation.RecipeUiModel
import com.mycompany.recipecomposeapp.core.model.toUiModel
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.mycompany.recipecomposeapp.core.ui.ScreenHeader
import com.mycompany.recipecomposeapp.features.recipes.ui.RecipeItem
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import kotlinx.coroutines.flow.map

@Composable
fun FavoritesScreen(
    drawableResId: Int,
    headerText: String,
    favoriteDataStore: FavoriteDataStoreManager,
    repository: RecipesRepositoryStub,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int, RecipeUiModel) -> Unit
) {

    val favoriteRecipesFlow = remember { favoriteDataStore.getFavoriteIdsFlow() }
        .map { ids -> ids.mapNotNull { repository.getRecipeById(it.toIntOrNull()) } }

    val favoriteRecipes by favoriteRecipesFlow.collectAsState(initial = emptyList())

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            imageResId = drawableResId,
            title = headerText
        )

        if (favoriteRecipes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = favoriteRecipes,
                    key = { it.id }
                ) { recipe: RecipeDto ->
                    RecipeItem(recipe = recipe.toUiModel(), onRecipeClick = onRecipeClick)
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
