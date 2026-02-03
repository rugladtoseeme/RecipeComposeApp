package com.mycompany.recipecomposeapp.data.model

import androidx.compose.runtime.Immutable
import com.mycompany.recipecomposeapp.ui.core.ui.Constants

data class CategoryDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)

@Immutable
data class CategoryUiModel(
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