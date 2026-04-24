package com.mycompany.recipecomposeapp.core.model

import com.mycompany.recipecomposeapp.core.ui.Constants
import fixtures.RecipeTestFixtures
import junit.framework.TestCase.assertEquals
import org.junit.Test

class RecipeDtoMapperTest {

    @Test
    fun `maps DTO to UI model correctly`() {
        val dto = RecipeTestFixtures.createRecipeDto()

        val result = dto.toUiModel()

        assertEquals(RecipeTestFixtures.recipeId, result.id)
        assertEquals(RecipeTestFixtures.recipeTitle, result.title)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        val imageUrl = "pancakes.jpg"
        val dto = RecipeTestFixtures.createRecipeDto(imageUrl = imageUrl)
        val result = dto.toUiModel()
        assertEquals(Constants.IMAGES_BASE_URL + imageUrl, result.imageUrl)
    }

    @Test
    fun `preserves full imageUrl starting with http`() {
        val fullImageUrl = "https://recipes.androidsprint.ru/pancakes.jpg"
        val dto = RecipeTestFixtures.createRecipeDto(imageUrl = fullImageUrl)
        val result = dto.toUiModel()
        assertEquals(fullImageUrl, result.imageUrl)
    }
}