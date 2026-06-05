package com.example.kosfinder.model

data class Review(
    val id: Int,
    val kost_id: Int,
    val user_id: Int,
    val rating: Int,
    val komentar: String?,
    val user: ReviewUser?
)

data class ReviewUser(
    val id: Int,
    val name: String
)