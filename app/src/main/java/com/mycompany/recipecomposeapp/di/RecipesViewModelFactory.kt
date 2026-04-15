package com.mycompany.recipecomposeapp.di

import androidx.lifecycle.SavedStateHandle
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.recipes.presentation.RecipesViewModel

class RecipesViewModelFactory(
    val savedStateHandle: SavedStateHandle,
    val repository: RecipesRepository
) :
    Factory<RecipesViewModel> {
    override fun create(): RecipesViewModel {
        return RecipesViewModel(
            savedStateHandle,
            repository
        )
    }
}