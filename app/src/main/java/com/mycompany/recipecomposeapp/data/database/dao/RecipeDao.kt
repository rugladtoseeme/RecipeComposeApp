package com.mycompany.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mycompany.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getRecipes(): Flow<List<RecipeEntity>>
}