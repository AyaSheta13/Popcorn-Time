package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.entity.Theme
import com.menna.myapplication.domain.repository.ThemeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ThemeManagerUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    fun getTheme(): Flow<Theme> {
        return repository.getTheme()
    }

    suspend fun saveTheme(theme: Theme) {
        repository.saveTheme(theme)
    }
}