package com.menna.popcorn.presentation.movie_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.menna.popcorn.domain.entity.Movie
import com.menna.popcorn.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesScreenModel(
    private val repo: MovieRepository
) : ScreenModel {

    private val _state = MutableStateFlow<List<Movie>>(emptyList())
    val state: StateFlow<List<Movie>> = _state

    init {
        loadMovies()
    }

    private fun loadMovies() {
        screenModelScope.launch {
            _state.value = repo.getPopularMovies()
        }
    }
}
