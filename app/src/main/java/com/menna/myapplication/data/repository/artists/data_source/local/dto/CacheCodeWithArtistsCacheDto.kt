package com.popcorntime.repository.artists.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.popcorntime.repository.utils.sharedDto.local.CacheCodeDto

data class CacheCodeWithArtistsCacheDto(
    @Embedded
    val cacheCode: CacheCodeDto,
    @Relation(
        parentColumn = "cacheCode",
        entityColumn = "artist_id_language",
        associateBy = Junction(CacheCodeArtistCrossRef::class)
    )
    val artists: List<ArtistCacheDto>,
)