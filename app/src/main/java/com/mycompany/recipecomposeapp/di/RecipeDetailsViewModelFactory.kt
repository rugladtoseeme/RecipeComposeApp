package com.mycompany.recipecomposeapp.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel

class RecipeDetailsViewModelFactory(
    val application: Application,
    val savedStateHandle: SavedStateHandle,
    val repository: RecipesRepository
) : Factory<RecipeDetailsViewModel> {
    override fun create(): RecipeDetailsViewModel {
        return RecipeDetailsViewModel(
            application,
            savedStateHandle,
            repository
        )
    }
}