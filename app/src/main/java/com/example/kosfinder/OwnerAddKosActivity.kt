package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.response.KostCreateResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class OwnerAddKosActivity : AppCompatActivity() {
    private var selectedThumbnail: Uri? = null
    private var selectedKamarMandi: Uri? = null
    private var selectedInterior: Uri? = null
    private var selectedDepan: Uri? = null
    private var currentImageTarget = ""
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                when (currentImageTarget) {
                    "thumbnail" -> {
                        selectedThumbnail = uri
                        findViewById<ImageView>(R.id.imgThumbnailPreview).setImageURI(uri)
                    }
                    "kamar_mandi" -> {
                        selectedKamarMandi = uri
                        findViewById<ImageView>(R.id.imgKamarMandiPreview).setImageURI(uri)
                    }
                    "interior" -> {
                        selectedInterior = uri
                        findViewById<ImageView>(R.id.imgInteriorPreview).setImageURI(uri)
                    }
                    "depan" -> {
                        selectedDepan = uri
                        findViewById<ImageView>(R.id.imgDepanPreview).setImageURI(uri)
                    }
                }
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_add_kos)
        // ================= NAVBAR OWNER =================

// Tombol navbar untuk halaman tambah kos
        val navTambahKos = findViewById<LinearLayout>(R.id.navTambahKos)

// Tombol navbar untuk halaman list kos milik owner
        val navListKosOwner = findViewById<LinearLayout>(R.id.navListKosOwner)

        val navPesananOwner = findViewById<LinearLayout>(R.id.navOwnerBooking)

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
        }

// Untuk sementara halaman Kos Saya belum dibuat,
// jadi tampilkan pesan dulu
        navListKosOwner.setOnClickListener {
            startActivity(Intent(this, OwnerKosActivity::class.java))
        }

// Saat tombol profil diklik,
// pindah ke ProfileActivity
        navProfileOwner.setOnClickListener {
            startActivity(
                Intent(this, ProfileOwnerActivity::class.java)
            )
        }

        navPesananOwner.setOnClickListener {
            startActivity(Intent(this, OwnerBookingActivity::class.java))
        }

// ================= END NAVBAR OWNER =================

        val etNamaKos = findViewById<EditText>(R.id.etNamaKos)
        val etAlamatKos = findViewById<EditText>(R.id.etAlamatKos)
        val etHargaKos = findViewById<EditText>(R.id.etHargaKos)
        val spinnerJenisKos = findViewById<Spinner>(R.id.spinnerJenisKos)
        val etJumlahKamar = findViewById<EditText>(R.id.etJumlahKamar)
        val etKamarTersedia = findViewById<EditText>(R.id.etKamarTersedia)
        val etFasilitas = findViewById<EditText>(R.id.etFasilitas)
        val btnSubmitKos = findViewById<Button>(R.id.btnSubmitKos)

        val jenisLabels = arrayOf("Kos Putra", "Kos Putri", "Kos Campur")
        val jenisValues = arrayOf("putra", "putri", "campur")

        val imgThumbnail = findViewById<ImageView>(R.id.imgThumbnailPreview)
        val imgKamarMandi = findViewById<ImageView>(R.id.imgKamarMandiPreview)
        val imgInterior = findViewById<ImageView>(R.id.imgInteriorPreview)
        val imgDepan = findViewById<ImageView>(R.id.imgDepanPreview)

        imgThumbnail.setOnClickListener {
            currentImageTarget = "thumbnail"
            pickImage.launch("image/*")
        }

        imgKamarMandi.setOnClickListener {
            currentImageTarget = "kamar_mandi"
            pickImage.launch("image/*")
        }

        imgInterior.setOnClickListener {
            currentImageTarget = "interior"
            pickImage.launch("image/*")
        }

        imgDepan.setOnClickListener {
            currentImageTarget = "depan"
            pickImage.launch("image/*")
        }
        spinnerJenisKos.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            jenisLabels
        )


        btnSubmitKos.setOnClickListener {
            val namaKos = etNamaKos.text.toString().trim()
            val alamat = etAlamatKos.text.toString().trim()
            val harga = etHargaKos.text.toString().trim()
            val jenisKos = jenisValues[spinnerJenisKos.selectedItemPosition]
            val jumlahKamar = etJumlahKamar.text.toString().trim()
            val kamarTersedia = etKamarTersedia.text.toString().trim()
            val fasilitasText = etFasilitas.text.toString().trim()

            if (
                namaKos.isEmpty() ||
                alamat.isEmpty() ||
                harga.isEmpty() ||
                jumlahKamar.isEmpty() ||
                kamarTersedia.isEmpty()
            ) {
                Toast.makeText(this, "Data kos wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fasilitasList = fasilitasText
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            val fasilitasJson = fasilitasList.joinToString(
                prefix = "[\"",
                separator = "\",\"",
                postfix = "\"]"
            )

            val token = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                .getString("token", null)

            if (token.isNullOrEmpty()) {
                Toast.makeText(this, "Token tidak ditemukan, login ulang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mediaType = "text/plain".toMediaType()

            RetrofitClient.apiService.tambahKos(
                token = "Bearer $token",
                namaKos = namaKos.toRequestBody(mediaType),
                alamat = alamat.toRequestBody(mediaType),
                harga = harga.toRequestBody(mediaType),
                jenisKos = jenisKos.toRequestBody(mediaType),
                jumlahKamar = jumlahKamar.toRequestBody(mediaType),
                kamarTersedia = kamarTersedia.toRequestBody(mediaType),
                fasilitas = fasilitasJson.toRequestBody(mediaType),
                thumbnail = uriToPart(selectedThumbnail, "thumbnail"),
                gambar_kamar_mandi = uriToPart(selectedKamarMandi, "gambar_kamar_mandi"),
                gambar_interior = uriToPart(selectedInterior, "gambar_interior"),
                gambar_depan = uriToPart(selectedDepan, "gambar_depan")
            ).enqueue(object : Callback<KostCreateResponse> {
                override fun onResponse(
                    call: Call<KostCreateResponse>,
                    response: Response<KostCreateResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(
                            this@OwnerAddKosActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()

                    } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@OwnerAddKosActivity,
                        "Gagal ${response.code()}: $error",
                        Toast.LENGTH_LONG
                    ).show()
                }
                }

                override fun onFailure(call: Call<KostCreateResponse>, t: Throwable) {
                    Toast.makeText(
                        this@OwnerAddKosActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
    private fun uriToPart(uri: Uri?, fieldName: String): MultipartBody.Part? {
        return try {
            if (uri == null) return null

            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(cacheDir, "$fieldName.jpg")
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            val requestBody = file.asRequestBody("image/*".toMediaType())

            MultipartBody.Part.createFormData(
                fieldName,
                file.name,
                requestBody
            )
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal proses gambar: ${e.message}", Toast.LENGTH_LONG).show()
            null
        }
    }

}