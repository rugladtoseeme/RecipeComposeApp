package fixtures

import com.mycompany.recipecomposeapp.core.model.CategoryDto

object CategoryTestFixtures {

    fun createCategoryDto(
        id: Int = 1,
        title: String = "Паста",
        description: String = "Аппетитная традиционная итальянская паста.",
        imageUrl: String = "pasta.jpg"
    ): CategoryDto = CategoryDto(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl
    )

    fun createCategoryDtoList(count: Int = 3): List<CategoryDto> =
        List(count) { index -> createCategoryDto(id = index + 1, title = "Категория ${index + 1}") }
}