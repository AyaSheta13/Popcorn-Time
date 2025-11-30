package com.popcorntime.repository.series.data_source.remote

import com.popcorntime.entity.Genre
import com.popcorntime.entity.Series
import com.popcorntime.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.popcorntime.repository.utils.TimeUtils

fun SeriesRemoteDto.toEntity(allGenres: List<Genre> = emptyList()): Series {
    return Series(
        id = id ?: 0L,
        title = name ?: "",
        rating = voteAverage?.times(0.5f) ?: 0f,
        posterPath = posterPath ?: "",
        trailerPath = "",
        genres = allGenres.filter { genreIds?.contains(it.id) == true },
        overview = overview ?: "",
        releaseDate = releaseDate?.let { TimeUtils.dateToLong(it)  } ?: 0L,
        seasonsCount = 1,
    )
}

@JvmName("toEntitySeries")
fun List<SeriesRemoteDto>.toEntityList(): List<Series> {
    return map { it.toEntity() }
}
