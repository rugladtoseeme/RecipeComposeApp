package com.mycompany.recipecomposeapp.ui.favorites

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
import com.mycompany.recipecomposeapp.data.model.RecipeDto
import com.mycompany.recipecomposeapp.data.model.RecipeUiModel
import com.mycompany.recipecomposeapp.data.model.toUiModel
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.mycompany.recipecomposeapp.ui.core.ui.ScreenHeader
import com.mycompany.recipecomposeapp.ui.recipes.RecipeItem
import com.mycompany.recipecomposeapp.util.FavoriteDataStoreManager
import kotlinx.coroutines.flow.map

@Composable
fun FavoritesScreen(
    drawableResId: Int,
    headerText: String,
    favoriteDataStore: FavoriteDataStoreManager,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int, RecipeUiModel) -> Unit
) {

    val favoriteRecipesFlow  = favoriteDataStore.getFavoriteIdsFlow()
        .map { ids -> ids.mapNotNull { RecipesRepositoryStub.getRecipeById(it.toIntOrNull()) } }

    val favoriteRecipes by favoriteRecipesFlow.collectAsState(initial = emptyList())

        Column(modifier = modifier.fillMaxWidth()) {
            ScreenHeader(
                imageResId = drawableResId,
                title = headerText
            )

            if (favoriteRecipes.isNotEmpty())
            {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = favoriteRecipes,
                    ) { recipe: RecipeDto ->
                        RecipeItem(recipe = recipe.toUiModel(), onRecipeClick = onRecipeClick)
                    }
                }

            }
            else{
                Spacer(Modifier.weight(weight = 1f))

                Text(
                    text = "Вы еще не добавили ни одного рецепта в избранное",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally).padding(16.dp),
                    fontSize = 30.sp,
                    lineHeight = 25.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.weight(weight = 1f))
            }
        }
    }
