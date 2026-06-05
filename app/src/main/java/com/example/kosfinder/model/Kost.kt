package com.example.kosfinder.model

import android.R

data class Kost(
    val id: Int,
    val owner_id: Int,
    val jenis_kost: String,
    val nama_kost: String,
    val alamat: String,
    val harga_per_bulan: Int,
    val jumlah_kamar: Int,
    val kamar_tersedia: Int,
    val fasilitas: List<String>?,
    val thumbnail: String?,
    val gambar_kamar_mandi: String?,
    val gambar_interior:String?,
    val gambar_depan:String?
)