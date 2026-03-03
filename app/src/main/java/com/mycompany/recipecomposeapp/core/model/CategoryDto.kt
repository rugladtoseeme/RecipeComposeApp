package com.mycompany.recipecomposeapp.core.model
import com.mycompany.recipecomposeapp.features.categories.presentation.model.CategoryUiModel
import com.mycompany.recipecomposeapp.core.ui.Constants

data class CategoryDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)

fun CategoryDto.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = if (imageUrl.startsWith("http")) imageUrl else Constants.ASSETS_URI_PREFIX + imageUrl
)