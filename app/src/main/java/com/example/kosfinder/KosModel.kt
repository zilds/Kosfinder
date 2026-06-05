package com.example.kosfinder

data class KosModel(
    val id: Int,
    val tipe: String,
    val nama: String,
    val lokasi: String,
    val harga: String,
    val thumbnail: String?,
    val gambarKamarMandi: String?,
    val gambarInterior: String?,
    val gambarDepan: String?,
    val fasilitas:List<String>?,
    val jumlahKamar: Int,
    val kamarTersedia: Int
)