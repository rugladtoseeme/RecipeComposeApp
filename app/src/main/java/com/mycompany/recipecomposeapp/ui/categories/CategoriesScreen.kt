package com.mycompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.R
import com.mycompany.recipecomposeapp.data.model.toUiModel
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.mycompany.recipecomposeapp.ui.core.ui.ScreenHeader


private val categories = RecipesRepositoryStub.getCategories()

@Composable
fun CategoriesScreen(
    onCategoryClick: (Int) -> Unit,
    drawableResId: Int,
    headerText: String,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            drawableResId,
            headerText
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(categories, key = { it.id }) { category ->
                CategoryItem(
                    onClick = { onCategoryClick(category.id) },
                    categoryUiModel = category.toUiModel()
                )
            }
        }
    }
}


@Composable
@Preview
fun CategoriesScreenPreview() {
    ScreenHeader(imagePainter = R.drawable.img_categories_header, title = "Заголовок")
}