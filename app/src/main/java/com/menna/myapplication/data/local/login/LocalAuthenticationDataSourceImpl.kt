package com.popcorntime.local.login

import com.popcorntime.local.login.dao.LoginDao
import com.popcorntime.repository.login.data_source.local.LocalAuthenticationDataSource
import com.popcorntime.repository.login.data_source.local.dto.SessionIdDto
import javax.inject.Inject

class LocalAuthenticationDataSourceImpl @Inject constructor(
    private val loginDao: LoginDao
) : LocalAuthenticationDataSource {
    override suspend fun saveSessionId(sessionId: String) {
        loginDao.saveSessionId(
            sessionId = SessionIdDto(sessionId = sessionId)
        )
    }

    override suspend fun getSessionId() = loginDao.getSessionId().getOrNull(0)?.sessionId ?: ""
}