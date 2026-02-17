package com.mycompany.recipecomposeapp.ui.core.ui

object Constants {
    const val ASSETS_URI_PREFIX = "file:///android_asset/"

    const val DEEP_LINK_SCHEME = "recipeapp"
    const val DEEP_LINK_BASE_URL = "[object Object]"

    fun createRecipeDeepLink(recipeId: Int) = "$DEEP_LINK_BASE_URL/recipe/$recipeId"
}