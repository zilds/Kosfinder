package com.example.kosfinder

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.String

class BookingHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        // 1. Hubungkan komponen UI dengan ID yang ada di XML
        val btnBack = findViewById<TextView>(R.id.btnBack)
        val rvBookingHistory = findViewById<RecyclerView>(R.id.rvBookingHistory)

        // 2. Fungsi tombol kembali ke Profile
        btnBack.setOnClickListener {
            finish()
        }

        // 3. Membuat Dummy Data (Disamakan persis dengan struktur variabel KosModel)
        val listRiwayat = ArrayList<KosModel>()

        listRiwayat.add(
            KosModel(
                id = 0,
                tipe = "Kos Putri",
                nama = "Kos Putri Melati",
                lokasi = "Purwokerto Utara",
                harga = "Rp 750.000 / bulan",
                thumbnail = "gambar",
                gambarKamarMandi="gambar",
                gambarInterior="gambar",
                 gambarDepan="gambar",
                fasilitas=emptyList(),
                jumlahKamar = 0,
                kamarTersedia = 0
            )
        )



        // 4. Pasang LayoutManager dan Adapter ke RecyclerView
        rvBookingHistory.layoutManager = LinearLayoutManager(this)
        val adapter = KosAdapter(listRiwayat)
        rvBookingHistory.adapter = adapter
    }
}