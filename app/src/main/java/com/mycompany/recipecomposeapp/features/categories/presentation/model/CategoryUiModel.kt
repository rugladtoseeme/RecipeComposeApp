package com.mycompany.recipecomposeapp.features.categories.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)