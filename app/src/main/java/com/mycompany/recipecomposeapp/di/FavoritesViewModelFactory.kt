package com.mycompany.recipecomposeapp.di

import android.app.Application
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.favorites.presentation.FavoritesViewModel

class FavoritesViewModelFactory(val application: Application, val repository: RecipesRepository) :
    Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(application = application, repository = repository)
    }
}