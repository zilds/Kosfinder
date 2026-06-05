package com.example.kosfinder.response

data class OwnerBookingResponse(
    val success: Boolean,
    val message: String,
    val data: List<BookingOwnerData>
)

data class BookingOwnerData(
    val id: Int,
    val nama_penyewa: String,
    val no_hp: String,
    val tanggal_masuk: String,
    val durasi_sewa: String,
    val status: String,
    val status_pembayaran: String,
    val catatan: String?,
    val kost: BookingOwnerKost
)

data class BookingOwnerKost(
    val id: Int,
    val nama_kost: String
)