package com.mycompany.recipecomposeapp.di

import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.data.repository.RecipesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecipesRepository(
        impl: RecipesRepositoryImpl
    ): RecipesRepository
}