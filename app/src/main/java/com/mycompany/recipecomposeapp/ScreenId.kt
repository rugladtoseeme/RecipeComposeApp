package com.mycompany.recipecomposeapp

enum class ScreenId(val screenName: String) {

    SPLASH_SCREEN("Экран загрузки"),
    CATEGORIES_LIST("Категории"),
    CATEGORY("Категория"),
    FAVORITES("Избранное"),
    RECIPE("Рецепт");

    override fun toString() = screenName

}