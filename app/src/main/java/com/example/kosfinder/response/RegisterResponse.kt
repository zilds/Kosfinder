package com.example.kosfinder.response

import com.example.kosfinder.model.User

data class RegisterResponse(
    val message: String,
    val token: String,
    val user: User,
    val phone: String?
)