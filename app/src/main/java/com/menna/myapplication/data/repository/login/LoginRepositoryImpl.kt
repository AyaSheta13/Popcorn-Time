package com.popcorntime.repository.login

import com.popcorntime.domain.repository.LoginRepository
import com.popcorntime.repository.login.data_source.local.LocalAuthenticationDataSource
import com.popcorntime.repository.login.data_source.remote.RemoteLoginDataSource
import com.popcorntime.repository.utils.mappers.tryToCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteLoginDataSource: RemoteLoginDataSource,
    private val localAuthenticationDataSource: LocalAuthenticationDataSource
) : LoginRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            localAuthenticationDataSource.getSessionId()
                .also { sessionId -> remoteLoginDataSource.updateInterceptorToken(sessionId) }
        }
    }

    override suspend fun login(username: String, password: String) {
        tryToCall {
            var requestTokenResponse = remoteLoginDataSource.createRequestToken()
            requestTokenResponse = remoteLoginDataSource.authenticateRequestToken(
                username = username,
                password = password,
                requestToken = requestTokenResponse.toEntity()
            )
            val sessionId = remoteLoginDataSource.createSessionId(requestTokenResponse.toEntity())
            localAuthenticationDataSource.saveSessionId(sessionId.sessionId ?: "")
            remoteLoginDataSource.updateInterceptorToken(sessionId.sessionId ?: "")
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return tryToCall {
            localAuthenticationDataSource.getSessionId().isNotEmpty()
        }
    }

    override suspend fun logout() {
        tryToCall {
            localAuthenticationDataSource.saveSessionId("")
            remoteLoginDataSource.updateInterceptorToken("")
        }
    }
}