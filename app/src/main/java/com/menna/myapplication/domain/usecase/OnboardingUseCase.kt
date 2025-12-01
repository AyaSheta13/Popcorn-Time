package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.repository.OnboardingRepository

class OnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend fun setOnboardingStateAsCompleted() {
        onboardingRepository.setOnboardingStateAsCompleted()
    }

    suspend fun getOnboardingState(): Boolean {
        return onboardingRepository.getOnboardingState()
    }
}