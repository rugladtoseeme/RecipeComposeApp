package com.mycompany.recipecomposeapp.features.recipes.ui

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class RecipesComposeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<RecipesComposeScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("recipes_screen") }  // корень экрана
    ) {
    val loadingIndicator: KNode = child { hasTestTag("loading_indicator") }
    val categoriesGrid: KNode = child { hasTestTag("empty_state") }
    val categoryItem: KNode = child { hasTestTag("recipe_item") }
}