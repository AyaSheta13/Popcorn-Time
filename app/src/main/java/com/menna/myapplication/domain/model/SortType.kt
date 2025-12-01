package com.menna.myapplication.domain.model

enum class SortType (val sortBy: String) {
    POPULAR("popularity.desc"),
    LATEST("primary_release_date.desc");
}