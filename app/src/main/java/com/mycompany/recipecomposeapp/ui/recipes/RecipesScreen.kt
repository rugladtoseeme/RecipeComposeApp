package com.mycompany.recipecomposeapp.ui.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.data.model.RecipeDto
import com.mycompany.recipecomposeapp.data.model.toUiModel
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.mycompany.recipecomposeapp.ui.core.ui.ScreenHeader

@Composable
fun RecipesScreen(
    categoryId: Int,
    categoryTitle: String,
    drawableResId: Int,
    modifier: Modifier = Modifier
) {

    var recipes by remember { mutableStateOf(listOf<RecipeDto>()) }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(categoryId) {
        isLoading = true
        try {
            recipes = RecipesRepositoryStub.getRecipesByCategoryId(categoryId)
        } finally {
            isLoading = false
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            drawableResId,
            categoryTitle
        )

        if (isLoading) CircularProgressIndicator()
        else
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(items = recipes, key = { it.id }) { recipe ->
                    RecipeItem(recipe = recipe.toUiModel(), onRecipeClick = {})
                }
            }
    }
}