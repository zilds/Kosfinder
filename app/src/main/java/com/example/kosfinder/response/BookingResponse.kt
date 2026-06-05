package com.example.kosfinder.response

data class BookingResponse(
    val message: String,
    val booking: BookingData
)

data class BookingData(
    val id: Int,
    val user_id: Int,
    val kost_id: Int,
    val nama_penyewa: String,
    val no_hp: String,
    val email: String?,
    val jenis_kelamin: String,
    val tanggal_masuk: String,
    val durasi_sewa: String,
    val jumlah_penghuni: Int,
    val catatan: String?,
    val status: String,
    val status_pembayaran: String
)