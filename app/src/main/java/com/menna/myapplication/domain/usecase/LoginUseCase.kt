package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.repository.LoginRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend fun login(username: String, password: String) {
        loginRepository.login(username, password)
    }

    suspend fun isUserLoggedIn(): Boolean {
        return loginRepository.isUserLoggedIn()
    }

    suspend fun logout() {
        loginRepository.logout()
    }
}