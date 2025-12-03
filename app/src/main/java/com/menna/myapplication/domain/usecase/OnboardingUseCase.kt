package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.repository.OnboardingRepository
import jakarta.inject.Inject

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