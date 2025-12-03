package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.entity.Movie
import com.menna.myapplication.domain.entity.Series
import com.menna.myapplication.domain.repository.AccountRepository
import jakarta.inject.Inject

class GetRatedItemsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun getRatedSeries(page: Int): List<Pair<Series, Double>> {
        return accountRepository.getRatedSeries(page)
    }

    suspend fun getRatedMovies(page: Int): List<Pair<Movie, Double>> {
        return accountRepository.getRatedMovies(page)
    }
}