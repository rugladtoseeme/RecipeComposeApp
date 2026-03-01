package com.mycompany.recipecomposeapp

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mycompany.recipecomposeapp.data.model.RecipeUiModel
import com.mycompany.recipecomposeapp.data.model.toUiModel
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.mycompany.recipecomposeapp.ui.categories.CategoriesScreen
import com.mycompany.recipecomposeapp.ui.details.RecipeDetailsScreen
import com.mycompany.recipecomposeapp.ui.favorites.FavoritesScreen
import com.mycompany.recipecomposeapp.ui.navigation.BottomNavigation
import com.mycompany.recipecomposeapp.ui.navigation.Destination
import com.mycompany.recipecomposeapp.ui.recipes.RecipesScreen
import com.mycompany.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import com.mycompany.recipecomposeapp.util.FavoriteDataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val KEY_RECIPE_OBJECT = "recipe"

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {

    val context = LocalContext.current
    val favoriteDataStore = remember { FavoriteDataStoreManager(context) }

    val coroutineScope = rememberCoroutineScope()

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
                    },
                    favoritesCount = favoriteDataStore.getFavoriteCountFlow()
                        .collectAsState(initial = 0).value
                )
            }
        ) { paddingValues ->

            LaunchedEffect(deepLinkIntent) {
                deepLinkIntent?.data?.let { uri ->
                    val recipeId: Int? = when (uri.scheme) {
                        "recipeapp" ->
                            if (uri.host == "recipe") uri.pathSegments[0].toIntOrNull() else null

                        "https", "http" ->
                            if (uri.pathSegments[0] == "recipe") uri.pathSegments[1].toIntOrNull() else null

                        else -> null
                    }

                    if (recipeId != null) {
                        delay(100)
                        navController.navigate(Destination.Recipe.createRoute(recipeId))
                    }
                }
            }

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
                        modifier = Modifier.padding(paddingValues),
                        favoriteDataStore = favoriteDataStore,
                        onRecipeClick = { recipeId, recipe ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                KEY_RECIPE_OBJECT,
                                recipe
                            )
                            navController.navigate("recipe/$recipeId")
                        }
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
                        modifier = Modifier.padding(paddingValues),
                        onRecipeClick = { recipeId, recipe ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                KEY_RECIPE_OBJECT,
                                recipe
                            )
                            navController.navigate("recipe/$recipeId")
                        }
                    )
                }

                composable(
                    route = Destination.Recipe.route,
                    arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val recipe =
                        navController.previousBackStackEntry?.savedStateHandle?.get<RecipeUiModel>(
                            KEY_RECIPE_OBJECT
                        )

                    val isFavorite by favoriteDataStore
                        .isFavoriteFlow(recipe?.id ?: -1)
                        .collectAsState(initial = false)

                    RecipeDetailsScreen(
                        recipe = recipe,
                        modifier = Modifier.padding(paddingValues),
                        isFavorite = isFavorite,
                        onToggleFavorite = {
                            coroutineScope.launch {
                                if (isFavorite) favoriteDataStore.removeFavorite(recipe?.id)
                                else favoriteDataStore.addFavorite(recipe?.id)
                            }
                        },
                    )
                }

                composable(
                    route = Destination.Recipe.route,
                    arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                )
                { backStackEntry ->

                    val recipeId = backStackEntry.arguments?.getInt("recipeId")

                    val recipe = RecipesRepositoryStub.getRecipeById(recipeId)

                    val isFavorite by favoriteDataStore
                        .isFavoriteFlow(recipe?.id ?: -1)
                        .collectAsState(initial = false)

                    recipe?.let {
                        RecipeDetailsScreen(
                            recipe = recipe.toUiModel(),
                            modifier = Modifier.padding(paddingValues),
                            isFavorite = isFavorite,
                            onToggleFavorite = {
                                coroutineScope.launch {
                                    if (isFavorite) favoriteDataStore.removeFavorite(recipeId)
                                    else favoriteDataStore.addFavorite(recipeId)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun RecipesAppPreview() {
    RecipesApp(null)
}