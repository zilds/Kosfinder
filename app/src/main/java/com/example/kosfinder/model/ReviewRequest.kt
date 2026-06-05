package com.example.kosfinder.model

data class ReviewRequest(
    val kost_id: Int,
    val rating: Int,
    val komentar: String
)