package com.example.kosfinder.response

import com.example.kosfinder.model.Kost

data class KostCreateResponse(
    val success: Boolean,
    val message: String,
    val data: Kost
)