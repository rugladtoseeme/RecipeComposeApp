package com.mycompany.recipecomposeapp

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mycompany.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.mycompany.recipecomposeapp.core.ui.navigation.Destination
import com.mycompany.recipecomposeapp.core.ui.theme.RecipeComposeAppTheme
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.mycompany.recipecomposeapp.di.FavoritesViewModelFactory
import com.mycompany.recipecomposeapp.di.RecipeApplication
import com.mycompany.recipecomposeapp.di.RecipeDetailsViewModelFactory
import com.mycompany.recipecomposeapp.di.RecipesViewModelFactory
import com.mycompany.recipecomposeapp.features.categories.ui.CategoriesScreen
import com.mycompany.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import com.mycompany.recipecomposeapp.features.favorites.ui.FavoritesScreen
import com.mycompany.recipecomposeapp.features.recipes.ui.RecipesScreen
import kotlinx.coroutines.delay

const val KEY_RECIPE_OBJECT = "recipe"

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {

    val context = LocalContext.current
    val favoriteDataStore = remember { FavoriteDataStoreManager(context) }

    RecipeComposeAppTheme {

        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(
                    onFavoritesClick = {
                        navController.navigate("favorites") {
                            popUpTo("categories") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onCategoriesClick = {
                        navController.navigate("categories") {
                            popUpTo("categories") { inclusive = false }
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

                composable(route = "categories") { backStackEntry ->

                    val savedStateHandle = backStackEntry.savedStateHandle
                    CategoriesScreen(
                        onCategoryClick = { categoryId, categoryTitle, categoryImageUrl ->
                            savedStateHandle["categoryTitle"] = categoryTitle
                            savedStateHandle["categoryImageUrl"] = categoryImageUrl
                            navController.navigate(
                                Destination.Recipes.createRoute(
                                    categoryId,
                                    categoryTitle,
                                    categoryImageUrl
                                )
                            ) {
                                launchSingleTop = true
                            }
                        },
                        drawableResId = R.drawable.img_categories_header,
                        headerText = "КАТЕГОРИИ",
                        modifier = Modifier.padding(paddingValues),
                    )
                }

                composable(route = "favorites") { backStackEntry ->

                    val application = context.applicationContext as Application

                    val appContainer =
                        (LocalContext.current.applicationContext as RecipeApplication).appContainer

                    val viewModel = remember {
                        FavoritesViewModelFactory(
                            application,
                            appContainer.recipesRepository
                        ).create()
                    }

                    FavoritesScreen(
                        drawableResId = R.drawable.img_favorites_header,
                        headerText = "ИЗБРАННОЕ",
                        modifier = Modifier.padding(paddingValues),
                        viewModel = viewModel,
                        onRecipeClick = { recipeId, recipe ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                KEY_RECIPE_OBJECT,
                                recipe
                            )
                            navController.navigate("recipe/$recipeId")
                        },
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument("categoryId") { type = NavType.IntType },
                        navArgument("categoryTitle") { type = NavType.StringType },
                        navArgument("categoryImageUrl") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val savedStateHandle = backStackEntry.savedStateHandle

                    val appContainer =
                        (LocalContext.current.applicationContext as RecipeApplication).appContainer

                    val viewModel = remember(backStackEntry) {
                        RecipesViewModelFactory(
                            savedStateHandle = savedStateHandle,
                            repository = appContainer.recipesRepository
                        ).create()
                    }

                    RecipesScreen(
                        viewModel = viewModel,
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
                    val appContainer =
                        (LocalContext.current.applicationContext as RecipeApplication).appContainer

                    val savedStateHandle = backStackEntry.savedStateHandle

                    val application = context.applicationContext as Application

                    val viewModel = RecipeDetailsViewModelFactory(
                        application,
                        savedStateHandle,
                        appContainer.recipesRepository
                    ).create()

                    RecipeDetailsScreen(
                        modifier = Modifier.padding(paddingValues),
                        viewModel = viewModel
                    )
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