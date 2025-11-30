package com.popcorntime.repository.guest

import com.popcorntime.domain.repository.GuestRepository
import com.popcorntime.repository.guest.data_source.local.GuestDataSource
import javax.inject.Inject

class GuestRepositoryImpl @Inject constructor(
    private val dataSource: GuestDataSource
) : GuestRepository {
    override suspend fun setGuestState(enteredAsGuest: Boolean) {
        dataSource.setGuestState(enteredAsGuest)
    }

    override suspend fun getGuestState(): Boolean {
        return dataSource.getGuestState()
    }
}