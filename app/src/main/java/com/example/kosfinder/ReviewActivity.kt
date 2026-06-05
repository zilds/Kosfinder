package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.model.Review
import com.example.kosfinder.model.ReviewRequest
import com.example.kosfinder.model.ReviewUser
import com.example.kosfinder.response.ReviewResponse
import com.example.kosfinder.response.ReviewStoreResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Int

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val token = pref.getString("token", "")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Silakan login dulu untuk memberi review", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_review)

        val ratingBar = findViewById<RatingBar>(R.id.ratingBarKos)
        val etKomentar = findViewById<EditText>(R.id.etKomentarReview)
        val btnKirimReview = findViewById<Button>(R.id.btnKirimReview)

        val kostId = intent.getIntExtra("kost_id", 0)

        btnKirimReview.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val komentar = etKomentar.text.toString().trim()

            if (kostId == 0) {
                Toast.makeText(this, "ID kos tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rating == 0) {
                Toast.makeText(this, "Pilih rating bintang dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (komentar.isEmpty()) {
                Toast.makeText(this, "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ReviewRequest(
                kost_id = kostId,
                rating = rating,
                komentar = komentar
            )



            RetrofitClient.apiService.kirimReview(
                "Bearer $token",
                request
            ).enqueue(object : Callback<ReviewStoreResponse> {
                override fun onResponse(
                    call: Call<ReviewStoreResponse>,
                    response: Response<ReviewStoreResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ReviewActivity,
                            "Review berhasil dikirim. Terima kasih!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }  else {
                    val errorMsg = when (response.code()) {
                        409 -> "Kamu sudah pernah memberi review untuk kos ini"
                        401 -> "Sesi login habis, silakan login ulang"
                        422 -> "Data review tidak valid"
                        else -> "Gagal kirim review: ${response.code()}"
                    }

                    Toast.makeText(
                        this@ReviewActivity,
                        errorMsg,
                        Toast.LENGTH_LONG
                    ).show()
                }
                }

                override fun onFailure(call: Call<ReviewStoreResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ReviewActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

    }
}