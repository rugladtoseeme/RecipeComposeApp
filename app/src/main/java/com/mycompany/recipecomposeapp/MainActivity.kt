package com.mycompany.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.let { _ ->
            deepLinkIntent = intent
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }

        enableEdgeToEdge()

        val categoryRequest = Request.Builder()
            .url("https://recipes.androidsprint.ru/api/category")
            .build()

        val json = Json {
            ignoreUnknownKeys = true
        }

        try {

            threadPool.execute {

                val categoriesListJson = try {
                    client.newCall(categoryRequest).execute().body?.string()
                } catch (e: Exception) {
                    Log.e(
                        "MainActivity",
                        "Не удалось получить категории.",
                        e
                    )
                    throw e
                }

                Log.i(
                    "!!!",
                    "Выполняю запрос (категории) на потоке: ${Thread.currentThread().name}"
                )

                Log.i("!!!", categoriesListJson ?: "")

                val categoriesList: List<CategoryDto> =
                    json.decodeFromString<List<CategoryDto>>(categoriesListJson ?: "")

                categoriesList.forEach {
                    threadPool.execute {
                        val recipesRequest = Request.Builder()
                            .url("https://recipes.androidsprint.ru/api/category/${it.id}/recipes")
                            .build()

                        val recipesList = try {
                            json.decodeFromString<List<RecipeDto>>(
                                client.newCall(recipesRequest).execute().body?.string() ?: ""
                            )
                        } catch (e: Exception) {
                            Log.e(
                                "MainActivity",
                                "Не удалось получить рецепты из категории ${it.title}",
                                e
                            )
                            throw e
                        }

                        Log.i(
                            "!!!",
                            "Выполняю запрос (рецепты) на потоке: ${Thread.currentThread().name}"
                        )
                        Log.i(
                            "!!!",
                            "В категории ${it.title} получено ${recipesList.size} рецептов.\n"
                        )
                    }
                }

                Log.i("!!!", "получено: ${categoriesList.size} категорий рецептов")
                Log.i(
                    "!!!",
                    "получены следующие категории:\n ${
                        categoriesList.joinToString(
                            separator = ",\n",
                            postfix = "."
                        )
                    }"
                )

            }
            Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        } catch (e: Exception) {
            Log.e("MainActivity", e.message, e)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { _ ->
            deepLinkIntent = intent
        }
        setIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}