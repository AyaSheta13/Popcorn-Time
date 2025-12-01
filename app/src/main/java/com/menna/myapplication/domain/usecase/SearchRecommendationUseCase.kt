package com.menna.myapplication.domain.usecase

class GetSearchRecommendationUseCase @Inject constructor(
    private val searchRecommendationRepository: SearchRecommendationRepository
) {
    suspend operator fun invoke(query: String): List<String> {
        return searchRecommendationRepository.getSearchRecommendation(query)
    }
}