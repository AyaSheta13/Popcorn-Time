package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.repository.VersionRepository
import jakarta.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val repository: VersionRepository
) {
    fun getAppVersion(): String = repository.getAppVersion()
}