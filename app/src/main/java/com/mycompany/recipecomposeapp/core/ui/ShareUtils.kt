package com.mycompany.recipecomposeapp.core.ui

import android.content.Context
import android.content.Intent

fun shareRecipe(context: Context, recipeId: Int, recipeTitle: String?) {
    val shareLink = Constants.createRecipeDeepLink(recipeId)
    val shareText = "Попробуй этот рецепт: $recipeTitle\n$shareLink"

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Поделиться рецептом"))
}