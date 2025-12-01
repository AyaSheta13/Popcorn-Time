package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.repository.VersionRepository

class GetAppVersionUseCase @Inject constructor(
    private val repository: VersionRepository
) {
    fun getAppVersion(): String = repository.getAppVersion()
}