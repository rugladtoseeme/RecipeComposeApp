package com.mycompany.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.mycompany.recipecomposeapp.ui.navigation.BottomNavigation
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

            when (currentScreen){
                ScreenId.FAVORITES -> Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Избранное",
                        modifier = Modifier
                            .padding(paddingValues)
                            .align(Alignment.Center),
                        fontSize = 50.sp
                    )
                }

                ScreenId.CATEGORIES_LIST -> Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Категории",
                        modifier = Modifier
                            .padding(paddingValues)
                            .align(Alignment.Center),
                        fontSize = 50.sp
                    )
                }

                ScreenId.SPLASH_SCREEN -> Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Экран загрузки",
                        modifier = Modifier
                            .padding(paddingValues)
                            .align(Alignment.Center),
                        fontSize = 50.sp
                    )
                }

                ScreenId.RECIPE -> Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Рецепт",
                        modifier = Modifier
                            .padding(paddingValues)
                            .align(Alignment.Center),
                        fontSize = 50.sp
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