package com.mycompany.recipecomposeapp

enum class ScreenId(val screenName: String) {

    SPLASH_SCREEN("Экран загрузки"),
    CATEGORIES_LIST("Категории"),
    FAVORITES("Избранное"),
    RECIPES_LIST("Рецепты"),
    RECIPE("Рецепт");

    override fun toString() = screenName

}