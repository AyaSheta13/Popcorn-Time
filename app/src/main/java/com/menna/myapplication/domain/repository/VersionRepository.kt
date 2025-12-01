package com.menna.myapplication.domain.repository

interface VersionRepository {
    fun getAppVersion(): String
}