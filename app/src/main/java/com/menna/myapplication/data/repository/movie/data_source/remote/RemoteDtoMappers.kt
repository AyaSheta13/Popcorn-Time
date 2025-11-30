package com.popcorntime.repository.movie.data_source.remote

import com.popcorntime.entity.Genre
import com.popcorntime.entity.Movie
import com.popcorntime.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.popcorntime.repository.utils.TimeUtils

fun MovieRemoteDto.toEntity(allGenres: List<Genre> = emptyList()): Movie {
    return Movie(
        id = id?.toLong() ?: 0L,
        title = title ?: "",
        rating = voteAverage?.toFloat()?.times(0.5f) ?: 0f,
        posterPath = posterPath ?: "",
        genres = allGenres.filter { genreIds?.contains(it.id) == true },
        overview = overview.orEmpty(),
        releaseDate = releaseDate?.let { TimeUtils.dateToLong(it) } ?: 0L,
        runtimeMinutes = 0,
        trailerPath = "",
    )
}

@JvmName("toEntityMovie")
fun List<MovieRemoteDto>.toEntityList(): List<Movie> {
    return map { it.toEntity() }
}
