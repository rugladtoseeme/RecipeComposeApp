package com.mycompany.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mycompany.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("select * from recipes where category_id = :categoryId")
    fun getRecipesByCategoryId(categoryId: Int): Flow<List<RecipeEntity>>

    @Query("select * from recipes where id = :recipeId")
    fun getRecipeById(recipeId: Int): Flow<RecipeEntity?>

    @Query("select * from recipes where id IN (:recipeIds)")
    fun getRecipeByIdsList(recipeIds: List<Int>): Flow<List<RecipeEntity>>

}