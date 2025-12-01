package com.menna.myapplication.domain.di




@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    // Retrofit provider
    companion object {
        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit = retrofitProvider()

        @Provides
        @Singleton
        fun provideLoginApiService(retrofit: Retrofit): LoginApiService =
            retrofit.create(LoginApiService::class.java)

        @Provides
        @Singleton
        fun provideSeriesApiService(retrofit: Retrofit): SeriesApiService =
            retrofit.create(SeriesApiService::class.java)

        @Provides
        @Singleton
        fun provideArtistsApiService(retrofit: Retrofit): ArtistsApiService =
            retrofit.create(ArtistsApiService::class.java)

        @Provides
        @Singleton
        fun provideMovieApiService(retrofit: Retrofit): MovieApiService =
            retrofit.create(MovieApiService::class.java)

        @Provides
        @Singleton
        fun provideAccountApiService(retrofit: Retrofit): AccountApiService =
            retrofit.create(AccountApiService::class.java)
    }

    @Binds
    @Singleton
    abstract fun bindRemoteLoginDataSource(
        impl: RemoteLoginDataSourceImpl
    ): RemoteLoginDataSource

    @Binds
    @Singleton
    abstract fun bindArtistsRemoteDataSource(
        impl: RemoteArtistDataSourceImpl
    ): ArtistsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMoviesRemoteDataSource(
        impl: MoviesRemoteDataSourceImpl
    ): MoviesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindSeriesRemoteDataSource(
        impl: SeriesRemoteDataSourceImpl
    ): SeriesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAccountRemoteDataSource(
        impl: AccountRemoteDataSourceImpl
    ): AccountRemoteDataSource
}