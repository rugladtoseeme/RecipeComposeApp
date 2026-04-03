package com.mycompany.recipecomposeapp.features.favorites.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.recipecomposeapp.core.model.toUiModel
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.favorites.model.FavoritesUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(application: Application, private val repository: RecipesRepository) : AndroidViewModel(application) {
    private val favoriteManager = FavoriteDataStoreManager(application)
    private val _uiState: StateFlow<FavoritesUiState> =
        favoriteRecipesFlow().map { FavoritesUiState(it) }
            .stateIn(viewModelScope, initialValue = FavoritesUiState(emptyList()), started = SharingStarted.Lazily)

    val uiState = _uiState

    private fun favoriteRecipesFlow() = favoriteManager.getFavoriteIdsFlow().map { ids ->
        ids.mapNotNull { repository.getRecipe(it.toInt())?.toUiModel() }
    }
}