package com.mycompany.recipecomposeapp.core.model

import com.mycompany.recipecomposeapp.core.ui.Constants
import fixtures.RecipeTestFixtures
import junit.framework.TestCase.assertEquals
import org.junit.Test

class RecipeDtoMapperTest {

    @Test
    fun `maps DTO to UI model correctly`() {
        val dto = RecipeTestFixtures().createRecipeDto()

        val result = dto.toUiModel()

        assertEquals(RecipeTestFixtures().recipeId, result.id)
        assertEquals(RecipeTestFixtures().recipeTitle, result.title)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        val dto = RecipeDto(
            id = 1, title = "Панкейки",
            imageUrl = "pancakes.jpg",
            ingredients = listOf(),
            method = listOf()
        )
        val result = dto.toUiModel()
        assertEquals(Constants.IMAGES_BASE_URL + "pancakes.jpg", result.imageUrl)
    }

    @Test
    fun `preserves full imageUrl starting with http`() {
        val dto = RecipeDto(
            id = 1, title = "Завтраки",
            imageUrl = "https://recipes.androidsprint.ru/pancakes.jpg",
            ingredients = listOf(),
            method = listOf()
        )
        val result = dto.toUiModel()
        assertEquals("https://recipes.androidsprint.ru/pancakes.jpg", result.imageUrl)
    }
}