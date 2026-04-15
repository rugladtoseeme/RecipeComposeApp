package com.mycompany.recipecomposeapp.di

import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.categories.presentation.CategoriesViewModel

class CategoriesViewModelFactory(val repository: RecipesRepository) : Factory<CategoriesViewModel> {
    override fun create(): CategoriesViewModel {

        return CategoriesViewModel(
            repository = repository
        )
    }

}