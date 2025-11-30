package com.popcorntime.repository.login.data_source.remote

import com.popcorntime.repository.login.data_source.remote.dto.RequestTokenResponse
import com.popcorntime.repository.login.data_source.remote.dto.SessionIdResponse

interface RemoteLoginDataSource {
    suspend fun createRequestToken(): RequestTokenResponse

    suspend fun authenticateRequestToken(
        username: String,
        password: String,
        requestToken: String
    ): RequestTokenResponse

    suspend fun createSessionId(requestToken: String): SessionIdResponse

    suspend fun updateInterceptorToken(token: String)
}