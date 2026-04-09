package com.mycompany.recipecomposeapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mycompany.recipecomposeapp.core.model.IngredientDto
import com.mycompany.recipecomposeapp.core.model.Quantity
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import com.mycompany.recipecomposeapp.data.database.converter.Converter

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: String,
    val method: String
)

fun RecipeEntity.toDto() = RecipeDto(
    id = id,
    title = title,
    ingredients = Converter().fromString(ingredients).map {
        IngredientDto(
            name = it,
            quantity = Quantity.ByTaste
        )
    },
    method = Converter().fromString(method),
    imageUrl = imageUrl,
)
