package com.menna.myapplication.domain.usecase

import com.menna.myapplication.domain.entity.Artist
import com.menna.myapplication.domain.entity.Movie
import com.menna.myapplication.domain.entity.Series
import com.menna.myapplication.domain.repository.ArtistsRepository
import com.menna.myapplication.domain.repository.MoviesRepository
import com.menna.myapplication.domain.repository.SearchRepository
import com.menna.myapplication.domain.repository.SeriesRepository
import jakarta.inject.Inject

class ManageArtistUseCase @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val searchRepository: SearchRepository,
    private val moviesRepository: MoviesRepository,
    private val seriesRepository: SeriesRepository
) {
    suspend fun getArtistsByQuery(query: String, page: Int ): List<Artist> {
        return artistsRepository.getArtistsByQuery(query,page).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getArtistById(id: Long): Artist {
        return artistsRepository.getArtistById(id)
    }

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return moviesRepository.getMoviesOfArtist(artistId)
    }

    suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return seriesRepository.getSeriesOfArtist(artistId)
    }
}