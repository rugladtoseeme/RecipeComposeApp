package com.mycompany.recipecomposeapp.util

import android.content.Context
import androidx.core.content.edit

const val FAVOURITE_RECIPE_IDS_KEY = "favorite_recipe_ids"

class FavoritePrefsManager(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(FAVOURITE_RECIPE_IDS_KEY, Context.MODE_PRIVATE)

    fun isFavorite(recipeId: Int?) =
        sharedPreferences.getStringSet(FAVOURITE_RECIPE_IDS_KEY, emptySet())
            ?.contains(recipeId.toString())
            ?: false


    fun addToFavorites(recipeId: Int?) {
        val set = sharedPreferences.getStringSet(FAVOURITE_RECIPE_IDS_KEY, emptySet())
        val mutableSet = set?.toMutableSet() ?: mutableSetOf()
        mutableSet.add(recipeId.toString())
        sharedPreferences.edit {
            putStringSet(FAVOURITE_RECIPE_IDS_KEY, mutableSet)
        }
    }

    fun removeFromFavorites(recipeId: Int?) {
        val set = sharedPreferences.getStringSet(FAVOURITE_RECIPE_IDS_KEY, emptySet())
        val mutableSet = set?.toMutableSet() ?: mutableSetOf()
        mutableSet.remove(recipeId.toString())
        sharedPreferences.edit {
            putStringSet(FAVOURITE_RECIPE_IDS_KEY, mutableSet)
        }
    }

    fun getAllFavorites() =
        sharedPreferences.getStringSet(FAVOURITE_RECIPE_IDS_KEY, emptySet())
            ?.map { it.toInt() }
            ?.toSet() ?: setOf()
}