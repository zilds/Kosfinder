package com.example.kosfinder.response

import com.example.kosfinder.model.Kost

data class KostListResponse(
    val success: Boolean,
    val message: String,
    val data: List<Kost>
)