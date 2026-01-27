package com.mycompany.recipecomposeapp

enum class ScreenId(val screenName: String) {

    SPLASH_SCREEN("Экран загрузки"),
    CATEGORIES_LIST("Категории"),
    FAVORITES("Избранное"),
    RECIPE("Рецепт");

    override fun toString() = screenName

}