package com.menna.myapplication.domain.repository

interface SearchRecommendationRepository {
    suspend fun getSearchRecommendation(query: String): List<String>
}