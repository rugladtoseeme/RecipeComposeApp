package com.mycompany.recipecomposeapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object PreferencesKeys {
    val FAVORITE_RECIPE_IDS = stringSetPreferencesKey(FAVOURITE_RECIPE_IDS_KEY)
    val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "recipe_app_prefs",
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context = context,
                sharedPreferencesName = FAVOURITE_RECIPE_IDS_KEY
            )
        )
    }
)

class FavoriteDataStoreManager(val context: Context) {

    suspend fun isFavorite(recipeId: Int?): Boolean {
        val preferences = context.dataStore.data.first()
        val favoriteIds = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
        return favoriteIds.contains(recipeId.toString())
    }

    suspend fun addFavorite(recipeId: Int?) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            val updatedFavorites = currentFavorites + recipeId.toString()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updatedFavorites
        }
    }

    suspend fun removeFavorite(recipeId: Int?){
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            val updatedFavorites = currentFavorites - recipeId.toString()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updatedFavorites
        }
    }
}
