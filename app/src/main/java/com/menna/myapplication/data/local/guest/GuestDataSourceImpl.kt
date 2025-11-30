package com.popcorntime.local.guest

import android.content.Context
import com.popcorntime.local.data_store.DataStoreConstants
import com.popcorntime.local.data_store.MovioDataStore
import com.popcorntime.repository.guest.data_source.local.GuestDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GuestDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GuestDataSource {
    override suspend fun setGuestState(enteredAsGuest: Boolean) {
        MovioDataStore.setPrefValue(
            context = context,
            key = DataStoreConstants.GUEST_STATE,
            value = enteredAsGuest
        )
    }

    override suspend fun getGuestState(): Boolean {
        return MovioDataStore.getPrefValue(
            context = context,
            key = DataStoreConstants.GUEST_STATE,
            defaultValue = false
        ) as Boolean
    }
}