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
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)

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

        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        try {

            val thread = Thread {

                connection.connect()

                val categoriesListJson =
                    connection.getInputStream().bufferedReader().readText().trimIndent()


                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

                Log.i("!!!", categoriesListJson)

                val categoriesList: List<CategoryDto> =
                    Json.decodeFromString<List<CategoryDto>>(categoriesListJson)

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

            thread.start()
        } catch (e: Exception) {
            Log.e("MainActivity", e.message, e)
        } finally {
            connection.disconnect()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { _ ->
            deepLinkIntent = intent
        }
        setIntent(intent)
    }
}