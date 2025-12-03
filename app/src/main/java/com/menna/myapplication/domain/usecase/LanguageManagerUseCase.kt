package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.entity.Language
import com.menna.myapplication.domain.repository.LanguageRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class LanguageManagerUseCase @Inject constructor(
    private val repository: LanguageRepository
) {

    fun getLanguage(): Flow<Language> {
        return repository.getLanguageFlow()
    }

    suspend fun saveLanguage(language: Language) {
        repository.saveLanguage(language)
    }
}