package com.mycompany.recipecomposeapp.features.categories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.recipecomposeapp.core.model.toUiModel
import com.mycompany.recipecomposeapp.data.repository.RecipesRepository
import com.mycompany.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val repository: RecipesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {

                val result = repository.getCategories()
                _uiState.update { state ->
                    state.copy(
                        categories = result.map { it.toUiModel() },
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
}