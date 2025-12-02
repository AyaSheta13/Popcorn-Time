package com.menna.popcorn.presentation.movie_details_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.menna.popcorn.domain.entity.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MovieDetailsUiState(
    val movie: Movie? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MovieDetailsScreenModel(
    initialMovie: Movie
) : ScreenModel {

    private val _uiState = MutableStateFlow(
        MovieDetailsUiState(
            movie = initialMovie,
            isFavorite = false
        )
    )
    val uiState: StateFlow<MovieDetailsUiState> = _uiState

    fun toggleFavorite() {
        screenModelScope.launch {
            val current = _uiState.value
            _uiState.value = current.copy(isFavorite = !current.isFavorite)
        }
    }

    fun showError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
