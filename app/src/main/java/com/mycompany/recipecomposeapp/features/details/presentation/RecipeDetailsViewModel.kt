package com.mycompany.recipecomposeapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.recipecomposeapp.core.model.Quantity
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.mycompany.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteManager = FavoriteDataStoreManager(application)

    private val _uiState = MutableStateFlow(
        RecipeDetailsUiState()
    )

    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    fun initializeWithRecipe(recipe: RecipeUiModel) {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val isFavorite = favoriteManager.isFavorite(recipe.id)
                _uiState.update { state ->
                    state.copy(
                        recipe = recipe.copy(isFavorite = isFavorite),
                        isFavorite = isFavorite,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Ошибка загрузки",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (_uiState.value.isFavorite) favoriteManager.removeFavorite(_uiState.value.recipe.id)
            else favoriteManager.addFavorite(_uiState.value.recipe.id)

            _uiState.update { state ->
                state.copy(
                    recipe = _uiState.value.recipe.copy(isFavorite = !state.isFavorite),
                    isFavorite = !state.isFavorite
                )
            }
        }
    }

    fun updatePortions(numberOfPortions: Int) {
        _uiState.update { state ->
            state.copy(
                numberOfPortions = numberOfPortions,
            )
        }
    }

    fun adjustIngredients(): List<IngredientUiModel> {
        val multiplier =
            _uiState.value.numberOfPortions.toFloat() / _uiState.value.recipe.servings.toFloat()
        return _uiState.value.recipe.ingredients.map { ingredient ->
            adjustIngredient(ingredient, multiplier)
        }
    }

    private fun adjustIngredient(
        ingredient: IngredientUiModel,
        multiplier: Float
    ): IngredientUiModel =
        ingredient.copy(
            quantity = if (ingredient.quantity is Quantity.Measured)
                ingredient.quantity.copy(amount = ingredient.quantity.amount * multiplier)
            else ingredient.quantity
        )
}


