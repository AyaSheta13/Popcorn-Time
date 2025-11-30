package com.popcorntime.local.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.popcorntime.local.account.AccountCacheDao
import com.popcorntime.local.cache.artist.ArtistsCacheDao
import com.popcorntime.local.cache.cacheCode.CacheCodeDao
import com.popcorntime.local.cache.genre.GenreDao
import com.popcorntime.local.cache.movie.MoviesCacheDao
import com.popcorntime.local.cache.reviews.ReviewDao
import com.popcorntime.local.cache.series.SeasonEpisodeCacheDao
import com.popcorntime.local.cache.series.SeriesCacheDao
import com.popcorntime.local.login.dao.LoginDao
import com.popcorntime.local.search.recent.dao.LocalRecentSearchDao
import com.popcorntime.repository.account.data_source.local.dto.AccountLocalDto
import com.popcorntime.repository.artists.data_source.local.dto.ArtistCacheDto
import com.popcorntime.repository.artists.data_source.local.dto.CacheCodeArtistCrossRef
import com.popcorntime.repository.login.data_source.local.dto.SessionIdDto
import com.popcorntime.repository.movie.data_source.local.dto.CacheCodeMovieCrossRef
import com.popcorntime.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.popcorntime.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.popcorntime.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto
import com.popcorntime.repository.search.data_source.local.dto.RecentSearchEntity
import com.popcorntime.repository.series.data_source.local.dto.CacheCodeEpisodeCrossRef
import com.popcorntime.repository.series.data_source.local.dto.CacheCodeSeasonCrossRef
import com.popcorntime.repository.series.data_source.local.dto.CacheCodeSeriesCacheCrossRef
import com.popcorntime.repository.series.data_source.local.dto.EpisodeCacheDto
import com.popcorntime.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.popcorntime.repository.series.data_source.local.dto.SeasonCacheDto
import com.popcorntime.repository.series.data_source.local.dto.SeriesGenreCacheCrossRef
import com.popcorntime.repository.series.data_source.local.dto.SeriesWithoutGenreCacheDto
import com.popcorntime.repository.utils.sharedDto.local.CacheCodeDto
import com.popcorntime.repository.utils.sharedDto.local.CacheCodeReviewCacheCrossRef
import com.popcorntime.repository.utils.sharedDto.local.ReviewCacheDto

@Database(
    entities = [
        ArtistCacheDto::class,
        CacheCodeArtistCrossRef::class,
        RecentSearchEntity::class,
        SessionIdDto::class,
        CacheCodeDto::class,
        AccountLocalDto::class,
        MovieWithoutGenreCacheDto::class,
        CacheCodeMovieCrossRef::class,
        GenreOfMovieCacheDto::class,
        MovieGenreCacheCrossRef::class,
        SeriesWithoutGenreCacheDto::class,
        CacheCodeSeriesCacheCrossRef::class,
        GenreOfSeriesCacheDto::class,
        SeriesGenreCacheCrossRef::class,
        ReviewCacheDto::class,
        CacheCodeReviewCacheCrossRef::class,
        SeasonCacheDto::class,
        EpisodeCacheDto::class,
        CacheCodeSeasonCrossRef::class,
        CacheCodeEpisodeCrossRef::class,
    ],
    version = 1,
    exportSchema = true,
)

abstract class MovioDataBase : RoomDatabase() {
    abstract fun recentSearchDao(): LocalRecentSearchDao

    abstract fun loginDao(): LoginDao

    abstract fun moviesCacheDao(): MoviesCacheDao

    abstract fun seriesCacheDao(): SeriesCacheDao

    abstract fun seasonEpisodeCacheDao(): SeasonEpisodeCacheDao

    abstract fun artistsCacheDao(): ArtistsCacheDao

    abstract fun genreDao(): GenreDao

    abstract fun cacheCodeDao(): CacheCodeDao

    abstract fun reviewDao(): ReviewDao

    abstract fun accountCacheDao(): AccountCacheDao
}