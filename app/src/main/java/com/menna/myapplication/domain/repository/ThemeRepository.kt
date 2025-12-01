package com.menna.myapplication.domain.repository

import com.menna.myapplication.domain.entity.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getTheme(): Flow<Theme>
    suspend fun saveTheme(theme: Theme)
}