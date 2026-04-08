package com.mycompany.recipecomposeapp.core.model

import com.mycompany.recipecomposeapp.features.categories.presentation.model.CategoryUiModel
import com.mycompany.recipecomposeapp.core.ui.Constants
import com.mycompany.recipecomposeapp.data.database.entity.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
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
    imageUrl = if (imageUrl.startsWith("http")) imageUrl else Constants.IMAGES_BASE_URL + imageUrl
)

fun CategoryDto.toEntity() = CategoryEntity(
    id = this.id,
    name = this.title,
    description = this.description,
    imageUrl = this.imageUrl
)