package com.mycompany.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mycompany.recipecomposeapp.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories order by categories.name")
    fun getCategories(): Flow<List<CategoryEntity>>
}