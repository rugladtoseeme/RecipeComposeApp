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

    @Test
    fun `mapper maps empty title correctly`() {
        val dto = CategoryDto(
            id = 1,
            title = "",
            description = "Утренние блюда",
            imageUrl = "breakfast.jpg"
        )
        val result = dto.toUiModel()
        assertEquals("", result.title)
    }

    @Test
    fun `mapper preserves very long description`() {
        val longDescription = "Утренние блюда ".repeat(50)
        val dto = CategoryDto(
            id = 1,
            title = "Завтраки",
            description = longDescription,
            imageUrl = "breakfast.jpg"
        )
        val result = dto.toUiModel()
        assertEquals(longDescription, result.description)
    }

}