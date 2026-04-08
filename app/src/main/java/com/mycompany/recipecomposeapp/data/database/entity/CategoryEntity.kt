package com.mycompany.recipecomposeapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mycompany.recipecomposeapp.core.model.CategoryDto

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String
)

fun CategoryEntity.toDto() = CategoryDto(
    id = id,
    title = name,
    description = description,
    imageUrl = imageUrl
)
