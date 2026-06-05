package com.example.kosfinder.model

data class BookingRequest(
    val kost_id: Int,
    val nama_penyewa: String,
    val no_hp: String,
    val email: String?,
    val jenis_kelamin: String,
    val tanggal_masuk: String,
    val durasi_sewa: Int,
    val jumlah_penghuni: Int,
    val catatan: String?
)