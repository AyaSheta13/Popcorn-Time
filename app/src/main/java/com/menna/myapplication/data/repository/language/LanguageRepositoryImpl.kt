package com.popcorntime.repository.language

import com.popcorntime.domain.repository.LanguageRepository
import com.popcorntime.entity.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val languageDataSource: LanguageDataSource
) : LanguageRepository {

    override suspend fun saveLanguage(language: Language) {
        languageDataSource.saveLanguage(language.code)
    }

    override suspend fun getLanguage(): String {
        return languageDataSource.getLanguage()
    }

    override fun getLanguageFlow(): Flow<Language> {
        return languageDataSource.getLanguageFlow().map { languageCode ->
            when (languageCode) {
                "ar" -> Language("ar", "العربية")
                else -> Language("en", "English")
            }
        }
    }
}