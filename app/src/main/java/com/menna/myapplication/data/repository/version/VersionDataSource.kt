package com.popcorntime.repository.version

interface VersionDataSource {
    fun getAppVersion(): String
}