package com.menna.myapplication.domain.di

import com.popcorntime.remote.account.AccountApiService
import com.popcorntime.remote.account.AccountRemoteDataSourceImpl
import com.popcorntime.remote.artists.ArtistsApiService
import com.popcorntime.remote.artists.RemoteArtistDataSourceImpl
import com.popcorntime.remote.login.LoginApiService
import com.popcorntime.remote.login.RemoteLoginDataSourceImpl
import com.popcorntime.remote.movie.MovieApiService
import com.popcorntime.remote.movie.MoviesRemoteDataSourceImpl
import com.popcorntime.remote.series.SeriesApiService
import com.popcorntime.remote.series.SeriesRemoteDataSourceImpl
import com.popcorntime.remote.utils.retrofit.retrofitProvider
import com.popcorntime.repository.account.data_source.remote.AccountRemoteDataSource
import com.popcorntime.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.popcorntime.repository.login.data_source.remote.RemoteLoginDataSource
import com.popcorntime.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.popcorntime.repository.series.data_source.remote.SeriesRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit


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