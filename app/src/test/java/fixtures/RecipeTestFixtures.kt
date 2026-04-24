package fixtures

import com.mycompany.recipecomposeapp.core.model.IngredientDto
import com.mycompany.recipecomposeapp.core.model.Quantity
import com.mycompany.recipecomposeapp.core.model.RecipeDto

object RecipeTestFixtures {

    val ingredientQuantity = "200"
    val ingredientUnitOfMeasure = "г"
    val ingredientDescription = "Паста"

    val recipeTitle = "Pasta Carbonara"

    val recipeId = 1

    val recipeMethod = listOf("Отварить", "Смешать")

    val recipeImageUrl = "pasta.jpg"

    fun createIngredientDto(
        quantity: String = ingredientQuantity,
        unitOfMeasure: String = ingredientUnitOfMeasure,
        description: String = ingredientDescription
    ) = IngredientDto(
        name = description,
        quantity = Quantity.Measured(
            amount = quantity.toDouble(),
            unit = unitOfMeasure
        )
    )

    fun createRecipeDto(
        id: Int = recipeId,
        title: String = recipeTitle ,
        ingredients: List<IngredientDto> = listOf(createIngredientDto()),
        method: List<String> = recipeMethod,
        imageUrl: String = recipeImageUrl
    ) = RecipeDto(
        id = id,
        title = title,
        ingredients = ingredients,
        method = method,
        imageUrl = imageUrl
    )

    fun createRecipeDtoList(count: Int = 3) =
        List(count) { index -> createRecipeDto(id = index + 1, title = "Рецепт ${index + 1}") }
}
