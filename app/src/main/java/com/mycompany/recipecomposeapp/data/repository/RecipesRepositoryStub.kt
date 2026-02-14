package com.mycompany.recipecomposeapp.data.repository

import com.mycompany.recipecomposeapp.data.model.CategoryDto
import com.mycompany.recipecomposeapp.data.model.IngredientDto
import com.mycompany.recipecomposeapp.data.model.Quantity
import com.mycompany.recipecomposeapp.data.model.RecipeDto

object RecipesRepositoryStub {

    private val categories = listOf(
        CategoryDto(
            id = 0,
            title = "Бургеры",
            description = "Рецепты всех популярных видов бургеров",
            imageUrl = "burger.png"
        ),
        CategoryDto(
            id = 1,
            title = "Десерты",
            description = "Самые вкусные рецепты десертов специально для вас",
            imageUrl = "dessert.png"
        ),
        CategoryDto(
            id = 2,
            title = "Пицца",
            description = "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            imageUrl = "pizza.png"
        ),
        CategoryDto(
            id = 3,
            title = "Рыба",
            description = "Печеная, жареная, сушеная, любая рыба на твой вкус",
            imageUrl = "fish.png"
        ),
        CategoryDto(
            id = 4,
            title = "Супы",
            description = "От классики до экзотики: мир в одной тарелке",
            imageUrl = "soup.png"
        ),
        CategoryDto(
            id = 5,
            title = "Салаты",
            description = "Хрустящий калейдоскоп под соусом вдохновения",
            imageUrl = "salad.png"
        ),
    )

    private val burgerRecipe = RecipeDto(
        id = 0,
        title = "Классический бургер с говядиной",
        ingredients = listOf(
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 0.5,
                    unit = "кг"
                ),
                name = "говяжий фарш",
            ),
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 1.0,
                    unit = "шт"
                ),
                name = "луковица, мелко нарезанная",
            ),
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 2.0,
                    unit = "зубч"
                ),
                name = "чеснок, измельченный",
            ),
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 4.0,
                    unit = "шт"
                ),
                name = "булочки для бургера",
            ),
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 4.0,
                    unit = "шт"
                ),
                name = "листа салата",
            ),
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 1.0,
                    unit = "шт"
                ),
                name = "помидор, нарезанный кольцами",
            ),
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 2.0,
                    unit = "ст. л."
                ),
                name = "горчица",
            ),
            IngredientDto(
                quantity = Quantity.Measured(
                    amount = 2.0,
                    unit = "ст. л."
                ),
                name = "кетчуп",
            ),
            IngredientDto(
                quantity = Quantity.ByTaste,
                name = "соль и черный перец",
            ),

            ),
        method = listOf(
            "1. В глубокой миске смешайте говяжий фарш, лук, чеснок, соль и перец. Разделите фарш на 4 равные части и сформируйте котлеты.",
            "2. Разогрейте сковороду на среднем огне. Обжаривайте котлеты с каждой стороны в течение 4-5 минут или до желаемой степени прожарки.",
            "3. В то время как котлеты готовятся, подготовьте булочки. Разрежьте их пополам и обжарьте на сковороде до золотистой корочки.",
            "4. Смазать нижние половинки булочек горчицей и кетчупом, затем положите лист салата, котлету, кольца помидора и закройте верхней половинкой булочки.",
            "5. Подавайте бургеры горячими с картофельными чипсами или картофельным пюре."
        ),
        imageUrl = "https://images.google.com"
    )

    private val burgerRecipes: List<RecipeDto> = listOf(burgerRecipe)
    private val dessertRecipes: List<RecipeDto> = listOf()
    private val fishRecipes: List<RecipeDto> = listOf()
    private val pizzaRecipes: List<RecipeDto> = listOf()
    private val saladRecipes: List<RecipeDto> = listOf()
    private val soupRecipes: List<RecipeDto> = listOf()

    fun getCategories() = categories

    fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto> {
        return when (categoryId) {
            0 -> burgerRecipes
            1 -> dessertRecipes
            2 -> pizzaRecipes
            3 -> fishRecipes
            4 -> soupRecipes
            5 -> saladRecipes

            else -> emptyList()
        }
    }

    fun getCategoryById(categoryId: Int): CategoryDto {
        return categories[categoryId]
    }

}