package com.popcorntime.repository.series.data_source.local

import com.popcorntime.repository.series.data_source.local.dto.CacheCodeWithEpisodesCacheDto
import com.popcorntime.repository.series.data_source.local.dto.CacheCodeWithSeasonsCacheDto
import com.popcorntime.repository.series.data_source.local.dto.EpisodeCacheDto
import com.popcorntime.repository.series.data_source.local.dto.SeasonCacheDto

interface SeasonEpisodeLocalDataSource {
    suspend fun insertCacheCodeWithSeasons(cacheCodeWithSeasons: CacheCodeWithSeasonsCacheDto)
    suspend fun insertCacheCodeWithEpisodes(cacheCodeWithEpisodes: CacheCodeWithEpisodesCacheDto)
    suspend fun getSeasonsByCacheCode(cacheCode: String): List<SeasonCacheDto>
    suspend fun getEpisodesByCacheCode(cacheCode: String): List<EpisodeCacheDto>
    suspend fun deleteExpiredCache(timestamp: Long)
}