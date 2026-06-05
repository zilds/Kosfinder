package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.response.KostListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OwnerKosActivity : AppCompatActivity() {

    private lateinit var rvDaftarKos: RecyclerView
    private lateinit var adapter: KosAdapter
    private val dataKos = ArrayList<KosModel>()
    private val allKos = ArrayList<KosModel>()
    override fun onResume() {
        super.onResume()
        getMyKosts()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_kos)

        rvDaftarKos = findViewById(R.id.rvDaftarKos)
        rvDaftarKos.layoutManager = LinearLayoutManager(this)

        adapter = KosAdapter(dataKos, true)
        rvDaftarKos.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchViewKos)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterKos(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterKos(newText ?: "")
                return true
            }
        })

        getMyKosts()
        // ================= NAVBAR OWNER =================

// Tombol navbar untuk halaman tambah kos
        val navTambahKos = findViewById<LinearLayout>(R.id.navTambahKos)

// Tombol navbar untuk halaman list kos milik owner
        val navListKosOwner = findViewById<LinearLayout>(R.id.navListKosOwner)

// Tombol navbar untuk menuju halaman profil
        val navProfileOwner = findViewById<LinearLayout>(R.id.navProfileOwner)

// Karena sekarang sudah berada di halaman tambah kos,
// tombol ini cukup menampilkan pesan saja
        navTambahKos.setOnClickListener {
            Toast.makeText(
                this,
                "Kamu sudah berada di halaman tambah kos",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, OwnerAddKosActivity::class.java))
        }

// Untuk sementara halaman Kos Saya belum dibuat,
// jadi tampilkan pesan dulu
        navListKosOwner.setOnClickListener {
            Toast.makeText(
                this,
                "Kamu sudah berada di halaman list kosmu",
                Toast.LENGTH_SHORT
            ).show()


        }

// Saat tombol profil diklik,
// pindah ke ProfileActivity
        navProfileOwner.setOnClickListener {
            startActivity(
                Intent(this, ProfileOwnerActivity::class.java)
            )
        }
        val navOwnerBooking = findViewById<LinearLayout>(R.id.navOwnerBooking)

        navOwnerBooking.setOnClickListener {
            startActivity(Intent(this, OwnerBookingActivity::class.java))
        }
    }

    private fun getMyKosts() {
        val token = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            .getString("token", "")

        RetrofitClient.apiService.getMyKosts("Bearer $token")
            .enqueue(object : Callback<KostListResponse> {
                override fun onResponse(
                    call: Call<KostListResponse>,
                    response: Response<KostListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val kostList = response.body()!!.data

                        dataKos.clear()
                        allKos.clear()

                        for (kost in kostList) {
                            val item = KosModel(
                                kost.id,
                                kost.jenis_kost.uppercase(),
                                kost.nama_kost,
                                kost.alamat,
                                "Rp ${kost.harga_per_bulan} / bulan",
                                kost.thumbnail,
                                kost.gambar_kamar_mandi,
                                kost.gambar_interior,
                                kost.gambar_depan,
                                kost.fasilitas,
                                kost.jumlah_kamar,
                                kost.kamar_tersedia
                            )

                            dataKos.add(item)
                            allKos.add(item)
                        }

                        adapter.notifyDataSetChanged()
                    } else {
                        val errorMsg = response.errorBody()?.string()

                        Toast.makeText(
                            this@OwnerKosActivity,
                            "Error ${response.code()}\n$errorMsg",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<KostListResponse>, t: Throwable) {
                    Toast.makeText(
                        this@OwnerKosActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun filterKos(keyword: String) {
        dataKos.clear()

        if (keyword.isEmpty()) {
            dataKos.addAll(allKos)
        } else {
            val filtered = allKos.filter {
                it.nama.contains(keyword, ignoreCase = true) ||
                        it.lokasi.contains(keyword, ignoreCase = true) ||
                        it.tipe.contains(keyword, ignoreCase = true)
            }

            dataKos.addAll(filtered)
        }

        adapter.notifyDataSetChanged()
    }

}