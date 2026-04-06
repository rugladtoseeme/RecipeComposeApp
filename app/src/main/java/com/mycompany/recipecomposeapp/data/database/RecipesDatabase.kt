package com.mycompany.recipecomposeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mycompany.recipecomposeapp.data.database.dao.CategoryDao
import com.mycompany.recipecomposeapp.data.database.entity.CategoryEntity

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipesDatabase() : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    companion object {
        fun buildDatabase(context: Context): RecipesDatabase = Room.databaseBuilder(
            name = "recipes_database",
            context = context,
            klass = RecipesDatabase::class.java
        ).fallbackToDestructiveMigration().build()
    }
}