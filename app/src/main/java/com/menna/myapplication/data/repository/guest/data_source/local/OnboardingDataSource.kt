package com.popcorntime.repository.guest.data_source.local

interface GuestDataSource {
    suspend fun setGuestState(enteredAsGuest: Boolean)
    suspend fun getGuestState(): Boolean
}