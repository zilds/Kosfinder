package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kosfinder.Adapter.ReviewAdapter
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.response.ReviewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKosActivity : AppCompatActivity() {

    private var kosId: Int = 0
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var txtJumlahUlasan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        kosId = intent.getIntExtra("EXTRA_KOST_ID", 0)

        val imgThumbnail = findViewById<ImageView>(R.id.imgDetailKos)
        val imgKamarMandi = findViewById<ImageView>(R.id.imgKamarMandi)
        val imgInterior = findViewById<ImageView>(R.id.imgInterior)
        val imgTampakDepan = findViewById<ImageView>(R.id.imgTampakDepan)

        val btnBackDetail = findViewById<ImageView>(R.id.btnBackDetail)
        val btnPesan = findViewById<Button>(R.id.btnPesan)
        val btnTulisReview = findViewById<Button>(R.id.btnTulisReview)

        val txtDetailNama = findViewById<TextView>(R.id.txtDetailNama)
        val txtDetailLokasi = findViewById<TextView>(R.id.txtDetailLokasi)
        val txtDetailHarga = findViewById<TextView>(R.id.txtDetailHarga)
        val txtDetailTipe = findViewById<TextView>(R.id.txtDetailTipe)
        val txtFasilitas = findViewById<TextView>(R.id.txtFasilitas)

        txtJumlahUlasan = findViewById(R.id.txtJumlahUlasan)

        val rvUlasan = findViewById<RecyclerView>(R.id.rvUlasan)
        reviewAdapter = ReviewAdapter()
        rvUlasan.layoutManager = LinearLayoutManager(this)
        rvUlasan.adapter = reviewAdapter

        val thumbnail = intent.getStringExtra("EXTRA_THUMBNAIL_KOS")
        val kamarMandi = intent.getStringExtra("EXTRA_KAMAR_MANDI")
        val interior = intent.getStringExtra("EXTRA_INTERIOR")
        val depan = intent.getStringExtra("EXTRA_DEPAN")

        val namaKos = intent.getStringExtra("EXTRA_NAMA_KOS") ?: "Nama Kos Tidak Tersedia"
        val lokasiKos = intent.getStringExtra("EXTRA_LOKASI_KOS") ?: "Lokasi Tidak Tersedia"
        val hargaKos = intent.getStringExtra("EXTRA_HARGA_KOS") ?: "Rp 0 / bln"
        val tipeKos = intent.getStringExtra("EXTRA_TIPE_KOS") ?: "Tipe Kos Tidak Tersedia"
        val fasilitas = intent.getStringArrayListExtra("EXTRA_FASILITAS")

        txtDetailNama.text = namaKos
        txtDetailLokasi.text = lokasiKos
        txtDetailHarga.text = hargaKos
        txtDetailTipe.text = tipeKos
        txtFasilitas.text = fasilitas?.joinToString("\n") { "• $it" } ?: "Fasilitas belum tersedia"

        val baseUrl = "https://editor-mountable-dreamy.ngrok-free.dev/storage/"

        Glide.with(this).load(baseUrl + thumbnail).placeholder(android.R.drawable.ic_menu_gallery).into(imgThumbnail)
        Glide.with(this).load(baseUrl + kamarMandi).placeholder(android.R.drawable.ic_menu_gallery).into(imgKamarMandi)
        Glide.with(this).load(baseUrl + interior).placeholder(android.R.drawable.ic_menu_gallery).into(imgInterior)
        Glide.with(this).load(baseUrl + depan).placeholder(android.R.drawable.ic_menu_gallery).into(imgTampakDepan)

        btnBackDetail.setOnClickListener {
            finish()
        }

        btnTulisReview.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("kost_id", kosId)
            startActivity(intent)
        }

        btnPesan.setOnClickListener {
            val token = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                .getString("token", "")

            if (!token.isNullOrEmpty()) {
                val intentBooking = Intent(this, BookingActivity::class.java)
                intentBooking.putExtra("KOST_ID", kosId)
                intentBooking.putExtra("EXTRA_NAMA_KOS", namaKos)
                intentBooking.putExtra("EXTRA_HARGA_KOS", hargaKos)
                startActivity(intentBooking)
            } else {
                Toast.makeText(this, "Kamu harus login terlebih dahulu untuk memesan!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        if (kosId != 0) {
            loadUlasan(kosId)
        } else {
            Toast.makeText(this, "ID kos tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (kosId != 0) {
            loadUlasan(kosId)
        }
    }

    private fun loadUlasan(kosId: Int) {
        RetrofitClient.apiService.getReviewByKost(kosId)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(
                    call: Call<ReviewResponse>,
                    response: Response<ReviewResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val dataReview = response.body()!!.data
                        reviewAdapter.setData(dataReview)
                        txtJumlahUlasan.text = "Ulasan Pengguna (${dataReview.size})"
                    } else {
                        Toast.makeText(
                            this@DetailKosActivity,
                            "Gagal mengambil ulasan: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    Toast.makeText(
                        this@DetailKosActivity,
                        "Error ulasan: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}