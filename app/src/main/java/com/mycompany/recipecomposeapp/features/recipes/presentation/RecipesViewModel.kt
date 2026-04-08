package com.mycompany.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.recipecomposeapp.core.model.toUiModel
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import kotlin.String

class RecipesViewModel(savedState: SavedStateHandle, repository: RecipesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RecipesUiState(
            categoryName = URLDecoder.decode(savedState.get<String>("categoryTitle") ?: "", "UTF-8")
                .uppercase(),
            categoryImageUrl = URLDecoder.decode(
                savedState.get<String>("categoryImageUrl") ?: "",
                "UTF-8"
            )
        )
    )
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                repository.getRecipesByCategoryId(
                    savedState.get<Int>("categoryId") ?: 0
                ).collect {
                    _uiState.update { state ->
                        state.copy(
                            recipes = it.map { it.toUiModel() },
                            isLoading = false,
                            emptyRecipeList = it.isEmpty()
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
}