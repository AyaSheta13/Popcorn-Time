package com.menna.myapplication.domain.entity

data class Review(
    val id: String,
    val author: String,
    val authorPhotoPath: String,
    val rating: Float,
    val date: Long,
    val description: String,
)
