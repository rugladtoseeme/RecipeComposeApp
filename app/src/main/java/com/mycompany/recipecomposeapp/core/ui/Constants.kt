package com.mycompany.recipecomposeapp.core.ui

object Constants {
    const val ASSETS_URI_PREFIX = "file:///android_asset/"

    const val DEEP_LINK_SCHEME = "recipeapp"
    const val DEEP_LINK_BASE_URL = "https://recipes.androidsprint.ru"

    const val IMAGES_BASE_URL = "https://recipes.androidsprint.ru/api/images/"

    fun createRecipeDeepLink(recipeId: Int) = "$DEEP_LINK_BASE_URL/recipe/$recipeId"
}