package com.popcorntime.local.cache.artist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.popcorntime.repository.artists.data_source.local.dto.ArtistCacheDto
import com.popcorntime.repository.artists.data_source.local.dto.CacheCodeArtistCrossRef
import com.popcorntime.repository.artists.data_source.local.dto.CacheCodeWithArtistsCacheDto

@Dao
interface ArtistsCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<ArtistCacheDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForCacheCodeAndArtist(crossRef: List<CacheCodeArtistCrossRef>)

    @Query("DELETE FROM ArtistCacheDto WHERE cachingTimestamp < :expirationTime")
    suspend fun deleteExpiredArtistCache(expirationTime: Long)

    @Query(
        "DELETE FROM CacheCodeArtistCrossRef " +
                "WHERE " +
                "NOT artist_id_language IN (SELECT artist_id FROM ArtistCacheDto) " +
                "OR NOT cacheCode IN (SELECT cacheCode FROM CacheCodeDto)"
    )
    suspend fun deleteCrossRefForNonExistingCacheCodeAndArtist()

    @Transaction
    @Query("SELECT * FROM CacheCodeDto WHERE cacheCode = :cacheCode")
    suspend fun getArtistsByCacheCode(cacheCode: String): CacheCodeWithArtistsCacheDto?
}