package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.response.KostListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var rvDaftarKos: RecyclerView
    private lateinit var adapter: KosAdapter
    private val dataKos = ArrayList<KosModel>()
    private val allKos = ArrayList<KosModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvDaftarKos = findViewById(R.id.rvDaftarKos)
        rvDaftarKos.layoutManager = LinearLayoutManager(this)

        adapter = KosAdapter(dataKos)
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
        getKosts()

        val navProfile = findViewById<LinearLayout>(R.id.navProfile)
        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }


    private fun getKosts() {
        RetrofitClient.apiService.getKosts()
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
                        Toast.makeText(
                            this@HomeActivity,
                            "Gagal: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<KostListResponse>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
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