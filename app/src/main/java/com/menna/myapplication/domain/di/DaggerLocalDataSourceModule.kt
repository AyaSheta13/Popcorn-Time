package com.menna.myapplication.domain.di



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