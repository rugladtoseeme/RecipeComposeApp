package com.mycompany.recipecomposeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.mycompany.recipecomposeapp.ui.categories.CategoriesScreen
import com.mycompany.recipecomposeapp.ui.favorites.FavoritesScreen
import com.mycompany.recipecomposeapp.ui.navigation.BottomNavigation
import com.mycompany.recipecomposeapp.ui.navigation.Destination
import com.mycompany.recipecomposeapp.ui.recipes.RecipesScreen
import com.mycompany.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    RecipeComposeAppTheme {

        var selectedCategoryTitle by remember { mutableStateOf("") }

        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(
                    onFavoritesClick = {
                        navController.navigate("favorites") {
                            popUpTo("favorites") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onCategoriesClick = {
                        navController.navigate("categories") {
                            popUpTo("favorites") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = "categories"
            ) {

                composable(route = "categories") {
                    CategoriesScreen(
                        onCategoryClick = { categoryId ->
                            selectedCategoryTitle =
                                RecipesRepositoryStub.getCategoryById(categoryId).title.uppercase()
                            navController.navigate(Destination.Recipes.createRoute(categoryId)) {
                                launchSingleTop = true
                            }
                        },
                        drawableResId = R.drawable.img_categories_header,
                        headerText = "КАТЕГОРИИ",
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                composable(route = "favorites") {
                    FavoritesScreen(
                        drawableResId = R.drawable.img_favorites_header,
                        headerText = "ИЗБРАННОЕ",
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                    RecipesScreen(
                        categoryId = categoryId,
                        categoryTitle = selectedCategoryTitle,
                        drawableResId = R.drawable.img_recipes_list_header,
                        modifier = Modifier.padding(paddingValues)
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