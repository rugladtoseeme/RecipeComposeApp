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
import androidx.lifecycle.lifecycleScope
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mycompany.recipecomposeapp.core.model.CategoryDto
import com.mycompany.recipecomposeapp.core.network.NetworkConfig
import com.mycompany.recipecomposeapp.core.network.api.RecipesApiService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity() : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    val contentType = "application/json".toMediaType()
    val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val apiService = retrofit.create(RecipesApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.let { _ ->
            deepLinkIntent = intent
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }

        enableEdgeToEdge()

        try {

            threadPool.execute {

                Log.i(
                    "!!!",
                    "Выполняю запрос (категории) на потоке: ${Thread.currentThread().name}"
                )

                lifecycleScope.launch {
                    val categoriesList: List<CategoryDto> = try {
                        apiService.getCategories()
                    } catch (e: Exception) {
                        Log.e(
                            "MainActivity",
                            "Не удалось получить категории.",
                            e
                        )
                        throw e
                    }
                    categoriesList.forEach {
                        threadPool.execute {

                            lifecycleScope.launch {
                                val recipesList = try {
                                    apiService.getRecipesByCategory(it.id)
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