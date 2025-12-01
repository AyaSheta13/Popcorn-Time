package com.menna.myapplication.domain.repository

import com.menna.myapplication.domain.entity.Language
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    fun getLanguageFlow(): Flow<Language>
    suspend fun getLanguage(): String
    suspend fun saveLanguage(language: Language)
}