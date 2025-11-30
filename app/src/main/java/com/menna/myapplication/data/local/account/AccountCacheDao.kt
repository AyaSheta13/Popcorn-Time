package com.popcorntime.local.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.vo.Dao
import com.popcorntime.repository.account.data_source.local.dto.AccountLocalDto

@Dao
interface AccountCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(accountDetail: AccountLocalDto)

    @Query("SELECT * FROM AccountLocalDto")
    suspend fun getAccount(): List<AccountLocalDto>

    @Query("DELETE FROM AccountLocalDto")
    suspend fun deleteAccount()
}