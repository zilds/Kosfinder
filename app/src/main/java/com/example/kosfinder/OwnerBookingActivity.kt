package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kosfinder.adapter.OwnerBookingAdapter
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.response.OwnerBookingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OwnerBookingActivity : AppCompatActivity() {

    private lateinit var rvOwnerBooking: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_booking)

        rvOwnerBooking = findViewById(R.id.rvOwnerBooking)
        rvOwnerBooking.layoutManager = LinearLayoutManager(this)

        loadOwnerBookings()
        val navTambahKos = findViewById<LinearLayout>(R.id.navTambahKos)
        val navListKosOwner = findViewById<LinearLayout>(R.id.navListKosOwner)
        val navPesananOwner = findViewById<LinearLayout>(R.id.navOwnerBooking)
        val navProfileOwner = findViewById<LinearLayout>(R.id.navProfileOwner)

        navTambahKos.setOnClickListener {
            startActivity(Intent(this, OwnerAddKosActivity::class.java))
        }

        navListKosOwner.setOnClickListener {
            startActivity(Intent(this, OwnerKosActivity::class.java))
        }

        navPesananOwner.setOnClickListener {
            Toast.makeText(this, "Kamu sudah berada di halaman tambah kos", Toast.LENGTH_SHORT).show()
        }

        navProfileOwner.setOnClickListener {
            startActivity(Intent(this, ProfileOwnerActivity::class.java))
        }
    }

    private fun loadOwnerBookings() {
        val token = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            .getString("token", "")

        RetrofitClient.apiService.getOwnerBookings("Bearer $token")
            .enqueue(object : Callback<OwnerBookingResponse> {
                override fun onResponse(
                    call: Call<OwnerBookingResponse>,
                    response: Response<OwnerBookingResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val bookings = response.body()!!.data
                        rvOwnerBooking.adapter = OwnerBookingAdapter(bookings)
                    } else {
                        Toast.makeText(
                            this@OwnerBookingActivity,
                            "Gagal mengambil data pemesanan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<OwnerBookingResponse>, t: Throwable) {
                    Toast.makeText(
                        this@OwnerBookingActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}