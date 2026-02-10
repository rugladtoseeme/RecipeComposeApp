package com.mycompany.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.data.model.RecipeUiModel
import com.mycompany.recipecomposeapp.ui.core.ui.ScreenHeader

@Composable
fun RecipeDetailsScreen(recipe: RecipeUiModel?, modifier: Modifier = Modifier) {

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            imageUrl = recipe?.imageUrl,
            title = recipe?.title ?: "РЕЦЕПТ",
        )

        LazyColumn {
            item {
                Text(
                    text = "ИНГРЕДИЕНТЫ",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                Text(
                    text = "СПОСОБ ПРИГОТОВЛЕНИЯ",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
