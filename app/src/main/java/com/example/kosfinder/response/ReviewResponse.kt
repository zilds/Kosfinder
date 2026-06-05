package com.example.kosfinder.response

import com.example.kosfinder.model.Review

data class ReviewResponse(
    val success: Boolean,
    val message: String,
    val data: List<Review>
)