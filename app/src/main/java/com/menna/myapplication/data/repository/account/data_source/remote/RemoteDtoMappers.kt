package com.popcorntime.repository.account.data_source.remote

import com.popcorntime.entity.Account
import com.popcorntime.entity.MediaList
import com.popcorntime.repository.account.data_source.remote.dto.MediaListDto
import com.popcorntime.repository.account.data_source.remote.dto.acount.AccountDto

internal fun AccountDto.toEntity() = Account(
    id = id ?: 0,
    name = name ?: "",
    username = username ?: "",
    avatarPath = avatar?.avatarPath?.avatarPath ?: ""
)

internal fun MediaListDto.toEntity() = MediaList(
    id = id,
    name = name,
    mediaCount = mediaCount
)