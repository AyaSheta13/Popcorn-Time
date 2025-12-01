package com.menna.myapplication.domain.entity

data class Episode(
    val id: Long,
    val episodeNumber: Int,
    val photoPath: String,
    val episodeName: String,
    val runtimeInMinutes: Int,
    val rating: Float,
    val seasonNumber: Int,
    val seriesId: Long,
)
