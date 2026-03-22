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
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.let { _ ->
            deepLinkIntent = intent
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }

        enableEdgeToEdge()

        val url = URL("https://recipes.androidsprint.ru/api/category")

        val json = Json {
            ignoreUnknownKeys = true
        }

        val connection: HttpURLConnection? = url.openConnection() as? HttpURLConnection
        try {

            threadPool.execute {

                connection?.connect()

                val categoriesListJson = try {
                    connection?.getInputStream()?.bufferedReader()?.readText()?.trimIndent()
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

                Log.i("!!!", categoriesListJson?:"")

                val categoriesList: List<CategoryDto> =
                    json.decodeFromString<List<CategoryDto>>(categoriesListJson?:"")

                categoriesList.forEach {
                    threadPool.execute {
                        val connection =
                            URL("https://recipes.androidsprint.ru/api/category/${it.id}/recipes").openConnection()
                        connection.connect()
                        val recipesList = try {
                            json.decodeFromString<List<RecipeDto>>(
                                connection.getInputStream().bufferedReader().readText().trimIndent()
                            )
                        }
                        catch (e: Exception){
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
        } finally {
            connection?.disconnect()
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