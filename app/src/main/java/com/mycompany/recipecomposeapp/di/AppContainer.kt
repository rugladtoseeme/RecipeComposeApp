package com.mycompany.recipecomposeapp.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mycompany.recipecomposeapp.BuildConfig
import com.mycompany.recipecomposeapp.core.network.NetworkConfig
import com.mycompany.recipecomposeapp.core.network.api.RecipesApiService
import com.mycompany.recipecomposeapp.data.database.RecipesDatabase
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {
    private val contentType = "application/json".toMediaType()
    val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(okHttpClient)
        .build()

    private val apiService = retrofit.create(RecipesApiService::class.java)

    private val recipesDb = RecipesDatabase.buildDatabase(context)
    val recipesRepository = RecipesRepositoryImpl(apiService, recipesDb)
}