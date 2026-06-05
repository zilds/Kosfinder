package com.example.kosfinder.response

import com.example.kosfinder.model.Review

data class ReviewStoreResponse(
    val success: Boolean,
    val message: String,
    val data: Review
)