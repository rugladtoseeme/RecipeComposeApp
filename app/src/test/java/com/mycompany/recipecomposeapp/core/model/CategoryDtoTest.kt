package com.mycompany.recipecomposeapp.core.model

import org.junit.Test
import org.junit.Assert.*

class CategoryDtoTest {
    @Test
    fun `converts DTO to UI model`() {
        val dto = CategoryDto(
            id = 1,
            title = "Завтраки",
            description = "Утренние блюда",
            imageUrl = "breakfast.jpg"
        )
        val result = dto.toUiModel()
        assertEquals("Завтраки", result.title)
    }

}