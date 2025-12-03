package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.repository.SearchRecommendationRepository
import jakarta.inject.Inject

class GetSearchRecommendationUseCase @Inject constructor(
    private val searchRecommendationRepository: SearchRecommendationRepository
) {
    suspend operator fun invoke(query: String): List<String> {
        return searchRecommendationRepository.getSearchRecommendation(query)
    }
}