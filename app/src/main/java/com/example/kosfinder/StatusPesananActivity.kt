package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatusPesananActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_pesanan)

        val txtHasilNama = findViewById<TextView>(R.id.txtHasilNama)
        val btnLanjutReview = findViewById<Button>(R.id.btnLanjutReview)

        // Tampilkan nama pengenal yang dioper dari form awal
        val nama = intent.getStringExtra("EXTRA_NAMA") ?: "User Kosfinder"
        txtHasilNama.text = nama

        // Alur penutup: Bawa user ke form review/rating ulasan akhir
        btnLanjutReview.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}