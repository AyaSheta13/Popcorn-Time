package com.popcorntime.repository.onboarding

import com.popcorntime.domain.repository.OnboardingRepository
import com.popcorntime.repository.onboarding.data_source.local.OnboardingDataSource
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataSource: OnboardingDataSource
) : OnboardingRepository {
    override suspend fun setOnboardingStateAsCompleted() {
        dataSource.setOnboardingStateAsCompleted()
    }

    override suspend fun getOnboardingState(): Boolean {
        return dataSource.getOnboardingState()
    }
}