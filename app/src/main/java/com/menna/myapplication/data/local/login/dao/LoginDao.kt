package com.popcorntime.local.login.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.popcorntime.repository.login.data_source.local.dto.SessionIdDto

@Dao
interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSessionId(sessionId: SessionIdDto)

    @Query("SELECT * FROM SessionIdDto")
    suspend fun getSessionId(): List<SessionIdDto>
}