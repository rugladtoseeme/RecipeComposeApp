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
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mycompany.recipecomposeapp.core.network.NetworkConfig
import com.mycompany.recipecomposeapp.core.network.api.RecipesApiService
import com.mycompany.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.mycompany.recipecomposeapp.core.ui.navigation.Destination
import com.mycompany.recipecomposeapp.core.ui.theme.RecipeComposeAppTheme
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryImpl
import com.mycompany.recipecomposeapp.features.categories.ui.CategoriesScreen
import com.mycompany.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel
import com.mycompany.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import com.mycompany.recipecomposeapp.features.favorites.ui.FavoritesScreen
import com.mycompany.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.mycompany.recipecomposeapp.features.recipes.ui.RecipesScreen
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

const val KEY_RECIPE_OBJECT = "recipe"

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {

    val context = LocalContext.current
    val favoriteDataStore = remember { FavoriteDataStoreManager(context) }

    val contentType = "application/json".toMediaType()
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val apiService = retrofit.create(RecipesApiService::class.java)

    val repository = remember {RecipesRepositoryImpl(apiService)}

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
                            popUpTo("categories") { inclusive = false  }
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

                composable(route = "categories") {backStackEntry ->

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
                        repository = repository
                    )
                }

                composable(route = "favorites") {
                    FavoritesScreen(
                        drawableResId = R.drawable.img_favorites_header,
                        headerText = "ИЗБРАННОЕ",
                        modifier = Modifier.padding(paddingValues),
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
                ) {backStackEntry ->

                    //val backStackEntry = navController.currentBackStackEntry
                    val savedStateHandle = backStackEntry.savedStateHandle

                    val viewModel: RecipesViewModel = remember(backStackEntry) {
                        RecipesViewModel(
                            savedStateHandle,
                            repository = repository
                        )
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

                    val savedStateHandle = backStackEntry.savedStateHandle

                    val application = context.applicationContext as Application

                    val viewModel: RecipeDetailsViewModel = remember(backStackEntry) {
                        RecipeDetailsViewModel(
                            savedStateHandle = savedStateHandle,
                            repository = repository,
                            application = application
                        )
                    }
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