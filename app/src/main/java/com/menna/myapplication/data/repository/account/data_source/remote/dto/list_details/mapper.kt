package com.popcorntime.repository.account.data_source.remote.dto.list_details

import com.popcorntime.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.popcorntime.repository.series.data_source.remote.dto.SeriesRemoteDto

fun ListDetailsItem.toRemoteMovieDto() = MovieRemoteDto(
    id = id,
    posterPath = posterPath,
    title = title,
    voteAverage = voteAverage,
    overview = overview,
    releaseDate = releaseDate,
    genreIds = genreIds,
)

fun ListDetailsItem.toRemoteSeriesDto() = SeriesRemoteDto(
    id = id,
    posterPath = posterPath,
    name = title,
    voteAverage = voteAverage?.toFloat() ?: 0f,
    overview = overview,
    releaseDate = releaseDate,
    genreIds = genreIds,
)