package com.mycompany.recipecomposeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mycompany.recipecomposeapp.data.database.converter.Converter
import com.mycompany.recipecomposeapp.data.database.dao.CategoryDao
import com.mycompany.recipecomposeapp.data.database.dao.RecipeDao
import com.mycompany.recipecomposeapp.data.database.entity.CategoryEntity
import com.mycompany.recipecomposeapp.data.database.entity.RecipeEntity

@Database(
    entities = [CategoryEntity::class, RecipeEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(value = [Converter::class])
abstract class RecipesDatabase() : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun recipeDao(): RecipeDao

    companion object {
        fun buildDatabase(context: Context): RecipesDatabase = Room.databaseBuilder(
            name = "recipes_database",
            context = context,
            klass = RecipesDatabase::class.java
        ).fallbackToDestructiveMigration().build()
    }
}