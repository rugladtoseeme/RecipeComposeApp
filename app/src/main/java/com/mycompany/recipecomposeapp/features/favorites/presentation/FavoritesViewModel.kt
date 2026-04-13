package com.mycompany.recipecomposeapp.features.favorites.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.recipecomposeapp.core.model.toUiModel
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(application: Application, private val repository: RecipesRepository) :
    AndroidViewModel(application) {
    private val favoriteManager = FavoriteDataStoreManager(application)
    private val _uiState: StateFlow<FavoritesUiState> =
        favoriteRecipesFlow().map { FavoritesUiState(it) }
            .stateIn(
                viewModelScope,
                initialValue = FavoritesUiState(emptyList()),
                started = SharingStarted.WhileSubscribed(5000)
            )

    val uiState = _uiState

    private fun favoriteRecipesFlow() =
        favoriteManager.getFavoriteIdsFlow().flatMapLatest { ids ->
            if (ids.isEmpty()) flowOf(emptyList())
            else {
                val flows = ids.map { repository.getRecipe(it.toInt()) }
                combine(flows) { recipes ->
                    recipes.filterNotNull().map { it.toUiModel() }
                }
            }
        }
}