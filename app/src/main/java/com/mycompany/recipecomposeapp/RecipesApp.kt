package com.mycompany.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mycompany.recipecomposeapp.ui.CategoriesScreen
import com.mycompany.recipecomposeapp.ui.favorites.FavoritesScreen
import com.mycompany.recipecomposeapp.ui.navigation.BottomNavigation
import com.mycompany.recipecomposeapp.ui.recipes.RecipesScreen
import com.mycompany.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    RecipeComposeAppTheme {

        var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES_LIST) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(
                    onFavoritesClick = { currentScreen = ScreenId.FAVORITES },
                    onCategoriesClick = { currentScreen = ScreenId.CATEGORIES_LIST }
                )
            }
        ) { paddingValues ->

            when (currentScreen) {
                ScreenId.FAVORITES -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FavoritesScreen(
                        drawableResId = R.drawable.img_favorites_header,
                        headerText = "ИЗБРАННОЕ"
                    )
                }

                ScreenId.CATEGORIES_LIST -> Box(modifier = Modifier.fillMaxWidth()) {

                    CategoriesScreen(
                        drawableResId = R.drawable.img_categories_header,
                        headerText = "КАТЕГОРИИ"
                    )
                }

                ScreenId.SPLASH_SCREEN -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Экран загрузки",
                        modifier = Modifier
                            .padding(paddingValues)
                            .align(Alignment.Center),
                        fontSize = 50.sp
                    )
                }

                ScreenId.RECIPE -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Рецепт",
                        modifier = Modifier
                            .padding(paddingValues)
                            .align(Alignment.Center),
                        fontSize = 50.sp
                    )
                }

                ScreenId.RECIPES_LIST -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    RecipesScreen(
                        drawableResId = R.drawable.img_recipes_list_header,
                        headerText = "РЕЦЕПТЫ"
                    )
                }
            }

        }
    }
}

@Composable
@Preview
fun RecipesAppPreview() {
    RecipesApp()
}