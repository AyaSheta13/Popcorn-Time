package com.popcorntime.remote.artists

import com.popcorntime.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.popcorntime.repository.movie.data_source.remote.dto.CreditResponse
import com.popcorntime.repository.utils.sharedDto.remote.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistsApiService {
    @GET("search/person")
    suspend fun getArtistsByQuery(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultResponse<ArtistRemoteDto>

    @GET("person/{id}")
    suspend fun getArtistById(
        @Path("id") id: Long
    ): ArtistRemoteDto

    @GET("movie/{movieId}/credits")
    suspend fun getMovieTopCast(
        @Path("movieId") movieId: Long,
        @Query("page") page: Int
    ): CreditResponse

    @GET("tv/{seriesId}/credits")
    suspend fun getSeriesTopCast(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): CreditResponse
}
