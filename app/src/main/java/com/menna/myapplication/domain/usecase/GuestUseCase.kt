package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.repository.GuestRepository

class ManageGuestUseCase @Inject constructor(
    private val guestRepository: GuestRepository
) {
    suspend fun setGuestState(enteredAsGuest: Boolean) {
        guestRepository.setGuestState(enteredAsGuest)
    }
    suspend fun getGuestState(): Boolean {
        return guestRepository.getGuestState()
    }
}