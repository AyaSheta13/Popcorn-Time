package com.popcorntime.local.cache.artist

import com.popcorntime.local.cache.cacheCode.CacheCodeDao
import com.popcorntime.repository.artists.data_source.local.ArtistsLocalDataSource
import com.popcorntime.repository.artists.data_source.local.dto.ArtistCacheDto
import com.popcorntime.repository.artists.data_source.local.dto.CacheCodeArtistCrossRef
import com.popcorntime.repository.artists.data_source.local.dto.CacheCodeWithArtistsCacheDto
import javax.inject.Inject

class ArtistsLocalDataSourceImpl @Inject constructor(
    private val artistsCacheDao: ArtistsCacheDao,
    private val cacheCodeDao: CacheCodeDao
) : ArtistsLocalDataSource {

    override suspend fun insertCacheCodeWithArtists(cacheCodeWithArtists: CacheCodeWithArtistsCacheDto) {
        artistsCacheDao.insertArtists(cacheCodeWithArtists.artists)
        cacheCodeDao.insertCacheCode(cacheCodeWithArtists.cacheCode)
        artistsCacheDao.insertCrossRefForCacheCodeAndArtist(
            CacheCodeArtistCrossRef.fromCacheCodeAndArtistList(
                cacheCode = cacheCodeWithArtists.cacheCode,
                artists = cacheCodeWithArtists.artists
            )
        )
    }

    override suspend fun getArtistsByCacheCode(cacheCode: String): List<ArtistCacheDto> {
        return artistsCacheDao.getArtistsByCacheCode(cacheCode)?.artists ?: emptyList()
    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        cacheCodeDao.deleteExpiredCacheCode(timestamp)
        artistsCacheDao.deleteExpiredArtistCache(timestamp)
        artistsCacheDao.deleteCrossRefForNonExistingCacheCodeAndArtist()
    }
}