package com.menna.myapplication.domain.di

import com.menna.myapplication.domain.repository.AccountRepository
import com.menna.myapplication.domain.repository.ArtistsRepository
import com.menna.myapplication.domain.repository.GuestRepository
import com.menna.myapplication.domain.repository.LanguageRepository
import com.menna.myapplication.domain.repository.LoginRepository
import com.menna.myapplication.domain.repository.MoviesRepository
import com.menna.myapplication.domain.repository.OnboardingRepository
import com.menna.myapplication.domain.repository.SearchRecommendationRepository
import com.menna.myapplication.domain.repository.SearchRepository
import com.menna.myapplication.domain.repository.SeriesRepository
import com.menna.myapplication.domain.repository.ThemeRepository
import com.menna.myapplication.domain.repository.VersionRepository
import com.popcorntime.repository.account.AccountRepositoryImpl
import com.popcorntime.repository.artists.ArtistsRepositoryImpl
import com.popcorntime.repository.guest.GuestRepositoryImpl
import com.popcorntime.repository.language.LanguageRepositoryImpl
import com.popcorntime.repository.login.LoginRepositoryImpl
import com.popcorntime.repository.movie.MovieRepositoryImpl
import com.popcorntime.repository.onboarding.OnboardingRepositoryImpl
import com.popcorntime.repository.search.SearchRecommendationRepositoryImpl
import com.popcorntime.repository.search.SearchRepositoryImpl
import com.popcorntime.repository.series.SeriesRepositoryImpl
import com.popcorntime.repository.theme.ThemeRepositoryImpl
import com.popcorntime.repository.version.VersionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindSearchRecommendationRepository(
        impl: SearchRecommendationRepositoryImpl
    ): SearchRecommendationRepository

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(
        impl: MovieRepositoryImpl
    ): MoviesRepository

    @Binds
    @Singleton
    abstract fun bindSeriesRepository(
        impl: SeriesRepositoryImpl
    ): SeriesRepository

    @Binds
    @Singleton
    abstract fun bindArtistsRepository(
        impl: ArtistsRepositoryImpl
    ): ArtistsRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindGuestRepository(
        impl: GuestRepositoryImpl
    ): GuestRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    @Singleton
    @Binds
    abstract fun bindThemeRepository(impl: ThemeRepositoryImpl): ThemeRepository

    @Singleton
    @Binds
    abstract fun bindLanguageRepository(impl: LanguageRepositoryImpl): LanguageRepository

    @Binds
    @Singleton
    abstract fun bindVersionRepository(
        impl: VersionRepositoryImpl
    ): VersionRepository
}