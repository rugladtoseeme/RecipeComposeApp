package com.mycompany.recipecomposeapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mycompany.recipecomposeapp.core.model.Quantity
import com.mycompany.recipecomposeapp.core.model.toUiModel
import com.mycompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle?,
    val repository: RecipesRepository
) : AndroidViewModel(application) {
    private val favoriteManager = FavoriteDataStoreManager(application)

    private val _uiState = MutableStateFlow(
        RecipeDetailsUiState()
    )


    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()
    private val recipeId: Int = savedStateHandle?.get<Int>("recipeId")
        ?: throw IllegalArgumentException("recipeId is required")

    init {
        loadRecipe(recipeId)
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, error = null) }

            val recipe = repository.getRecipe(recipeId)?.toUiModel()

            try {
                recipe?.let {
                    val isFavorite = favoriteManager.isFavorite(recipeId)
                    _uiState.update { state ->
                        state.copy(
                            recipe = recipe.copy(isFavorite = isFavorite),
                            isFavorite = isFavorite,
                            isLoading = false,
                            scaledIngredients = recipe.ingredients
                        )
                    }
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


//    fun initializeWithRecipe(recipe: RecipeUiModel) {
//
//        viewModelScope.launch {
//
//            _uiState.update { it.copy(isLoading = true, error = null) }
//
//            try {
//                val isFavorite = favoriteManager.isFavorite(recipe.id)
//                _uiState.update { state ->
//                    state.copy(
//                        recipe = recipe.copy(isFavorite = isFavorite),
//                        isFavorite = isFavorite,
//                        isLoading = false
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        error = e.message ?: "Ошибка загрузки",
//                        isLoading = false
//                    )
//                }
//            }
//        }
//    }

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
        _uiState.update { state ->
            state.copy(
                scaledIngredients = adjustIngredients(),
            )
        }
    }

    private fun adjustIngredients(): List<IngredientUiModel> {
        val multiplier =
            _uiState.value.numberOfPortions.toFloat()/_uiState.value.recipe.servings.toFloat()
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