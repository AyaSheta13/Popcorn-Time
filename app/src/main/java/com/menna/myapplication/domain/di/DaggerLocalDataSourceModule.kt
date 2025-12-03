package com.menna.myapplication.domain.di

import android.content.Context
import androidx.room.Room
import com.popcorntime.local.account.AccountCacheDao
import com.popcorntime.local.account.AccountLocalDataSourceImpl
import com.popcorntime.local.cache.artist.ArtistsCacheDao
import com.popcorntime.local.cache.artist.ArtistsLocalDataSourceImpl
import com.popcorntime.local.cache.cacheCode.CacheCodeDao
import com.popcorntime.local.cache.genre.GenreDao
import com.popcorntime.local.cache.movie.MoviesCacheDao
import com.popcorntime.local.cache.movie.MoviesLocalDataSourceImpl
import com.popcorntime.local.cache.reviews.ReviewDao
import com.popcorntime.local.cache.series.SeasonEpisodeCacheDao
import com.popcorntime.local.cache.series.SeasonEpisodeLocalDataSourceImpl
import com.popcorntime.local.cache.series.SeriesCacheDao
import com.popcorntime.local.cache.series.SeriesLocalDataSourceImpl
import com.popcorntime.local.guest.GuestDataSourceImpl
import com.popcorntime.local.language.LanguageDataStoreSourceImpl
import com.popcorntime.local.login.LocalAuthenticationDataSourceImpl
import com.popcorntime.local.login.dao.LoginDao
import com.popcorntime.local.onboarding.OnboardingDataSourceImpl
import com.popcorntime.local.search.recent.LocalRecentSearchDataSourceImpl
import com.popcorntime.local.search.recent.dao.LocalRecentSearchDao
import com.popcorntime.local.theme.ThemeDataStoreSourceImpl
import com.popcorntime.local.utils.MovioDataBase
import com.popcorntime.local.version.VersionDataSourceImpl
import com.popcorntime.repository.account.data_source.local.AccountLocalDataSource
import com.popcorntime.repository.artists.data_source.local.ArtistsLocalDataSource
import com.popcorntime.repository.guest.data_source.local.GuestDataSource
import com.popcorntime.repository.language.LanguageDataSource
import com.popcorntime.repository.login.data_source.local.LocalAuthenticationDataSource
import com.popcorntime.repository.movie.data_source.local.MoviesLocalDataSource
import com.popcorntime.repository.onboarding.data_source.local.OnboardingDataSource
import com.popcorntime.repository.search.data_source.local.LocalRecentSearchDataSource
import com.popcorntime.repository.series.data_source.local.SeasonEpisodeLocalDataSource
import com.popcorntime.repository.series.data_source.local.SeriesLocalDataSource
import com.popcorntime.repository.theme.ThemeDataSource
import com.popcorntime.repository.version.VersionDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovioDataBase {
        return Room.databaseBuilder(
            context,
            MovioDataBase::class.java,
            "MovioDataBase"
        ).build()
    }

    @Provides
    fun provideLoginDao(db: MovioDataBase): LoginDao = db.loginDao()

    @Provides
    fun provideMoviesCacheDao(db: MovioDataBase): MoviesCacheDao = db.moviesCacheDao()

    @Provides
    fun provideSeriesCacheDao(db: MovioDataBase): SeriesCacheDao = db.seriesCacheDao()

    @Provides
    fun provideSeasonEpisodeCacheDao(db: MovioDataBase): SeasonEpisodeCacheDao =
        db.seasonEpisodeCacheDao()

    @Provides
    fun provideArtistsCacheDao(db: MovioDataBase): ArtistsCacheDao = db.artistsCacheDao()

    @Provides
    fun provideGenreDao(db: MovioDataBase): GenreDao = db.genreDao()

    @Provides
    fun provideCacheCodeDao(db: MovioDataBase): CacheCodeDao = db.cacheCodeDao()

    @Provides
    fun provideReviewDao(db: MovioDataBase): ReviewDao = db.reviewDao()

    @Provides
    fun provideLocalRecentSearchDao(db: MovioDataBase): LocalRecentSearchDao = db.recentSearchDao()

    @Provides
    fun provideAccountCacheDao(db: MovioDataBase): AccountCacheDao = db.accountCacheDao()

    @Provides
    @Singleton
    fun provideLocalAuthenticationDataSource(
        loginDao: LoginDao
    ): LocalAuthenticationDataSource = LocalAuthenticationDataSourceImpl(loginDao)

    @Provides
    @Singleton
    fun provideLocalRecentSearchDataSource(
        dao: LocalRecentSearchDao
    ): LocalRecentSearchDataSource = LocalRecentSearchDataSourceImpl(dao)

    @Provides
    @Singleton
    fun provideMoviesLocalDataSource(
        movieDao: MoviesCacheDao,
        cacheCodeDao: CacheCodeDao,
        genreDao: GenreDao,
        reviewDao: ReviewDao
    ): MoviesLocalDataSource = MoviesLocalDataSourceImpl(
        moviesCacheDao = movieDao,
        cacheCodeDao = cacheCodeDao,
        genreDao = genreDao,
        reviewDao = reviewDao
    )

    @Provides
    @Singleton
    fun provideSeriesLocalDataSource(
        seriesDao: SeriesCacheDao,
        cacheCodeDao: CacheCodeDao,
        genreDao: GenreDao,
        reviewDao: ReviewDao
    ): SeriesLocalDataSource = SeriesLocalDataSourceImpl(
        seriesCacheDao = seriesDao,
        cacheCodeDao = cacheCodeDao,
        genreDao = genreDao,
        reviewDao = reviewDao
    )

    @Provides
    @Singleton
    fun provideSeasonEpisodeLocalDataSource(
        seasonEpisodeCacheDao: SeasonEpisodeCacheDao,
        cacheCodeDao: CacheCodeDao
    ): SeasonEpisodeLocalDataSource = SeasonEpisodeLocalDataSourceImpl(
        seasonEpisodeCacheDao = seasonEpisodeCacheDao,
        cacheCodeDao = cacheCodeDao
    )

    @Provides
    @Singleton
    fun provideArtistsLocalDataSource(
        artistsCacheDao: ArtistsCacheDao,
        cacheCodeDao: CacheCodeDao
    ): ArtistsLocalDataSource = ArtistsLocalDataSourceImpl(
        artistsCacheDao = artistsCacheDao,
        cacheCodeDao = cacheCodeDao
    )

    @Provides
    @Singleton
    fun provideOnboardingDataSource(
        @ApplicationContext context: Context
    ): OnboardingDataSource = OnboardingDataSourceImpl(
        context = context
    )

    @Provides
    @Singleton
    fun provideGuestDataSource(
        @ApplicationContext context: Context
    ): GuestDataSource = GuestDataSourceImpl(
        context = context
    )

    @Provides
    @Singleton
    fun provideAccountLocalDataSource(
        accountCacheDao: AccountCacheDao
    ): AccountLocalDataSource = AccountLocalDataSourceImpl(accountCacheDao)


    @Singleton
    @Provides
    fun provideThemeDataSource(@ApplicationContext context: Context): ThemeDataSource {
        return ThemeDataStoreSourceImpl(context)
    }

    @Singleton
    @Provides
    fun provideLanguageDataSource(@ApplicationContext context: Context): LanguageDataSource {
        return LanguageDataStoreSourceImpl(context)
    }
    @Provides
    @Singleton
    fun provideVersionDataSource(@ApplicationContext context: Context): VersionDataSource {
        return VersionDataSourceImpl(context)
    }

}