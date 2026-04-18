package com.mycompany.recipecomposeapp.features.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.recipecomposeapp.core.model.RecipeDto
import com.mycompany.recipecomposeapp.core.model.toUiModel
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val favoriteManager: FavoriteDataStoreManager, private val repository: RecipesRepository) :
    ViewModel() {
    private val _uiState: StateFlow<FavoritesUiState> =
        favoriteRecipesFlow().map { FavoritesUiState(it.map { it.toUiModel() }) }
            .stateIn(
                viewModelScope,
                initialValue = FavoritesUiState(emptyList()),
                started = SharingStarted.WhileSubscribed(5000)
            )

    val uiState = _uiState

    private fun favoriteRecipesFlow(): Flow<List<RecipeDto>> =
        favoriteManager.getFavoriteIdsFlow().flatMapLatest { ids ->
            if (ids.isEmpty()) flowOf(emptyList())
            else {
                val flows = repository.getRecipesByIdsList(ids.toList().map { it.toInt() })
                combine(flows) { recipes ->
                     recipes.flatMap { it.toList() }
                }
            }
        }
}