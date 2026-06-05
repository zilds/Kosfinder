package com.example.kosfinder

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.kosfinder.response.KostCreateResponse
import android.widget.Toast
import com.example.kosfinder.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AlertDialog
import okhttp3.ResponseBody
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MultipartBody
import java.io.File
import java.io.FileOutputStream

class EditKosActivity : AppCompatActivity() {

    private var kosId: Int = 0

    private lateinit var etNamaKos: EditText

    private lateinit var etAlamatKos: EditText
    private lateinit var etHargaKos: EditText
    private lateinit var etJumlahKamar: EditText
    private lateinit var etKamarTersedia: EditText
    private lateinit var etFasilitas: EditText
    private lateinit var spinnerJenisKos: Spinner
    private lateinit var btnUpdateKos: Button
    private lateinit var btnHapusKos: Button
    private fun deleteKos() {


        val token = getSharedPreferences(
            "USER_SESSION",
            MODE_PRIVATE
        ).getString("token", "") ?: ""

        RetrofitClient.apiService.deleteKost(
            "Bearer $token",
            kosId
        ).enqueue(object : Callback<KostCreateResponse> {

            override fun onResponse(
                call: Call<KostCreateResponse>,
                response: Response<KostCreateResponse>
            ) {

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@EditKosActivity,
                        "Kos berhasil dihapus",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
            }

            override fun onFailure(
                call: Call<KostCreateResponse>,
                t: Throwable
            ) {

                Toast.makeText(
                    this@EditKosActivity,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kos)

        kosId = intent.getIntExtra("id", 0)
        etNamaKos = findViewById(R.id.etNamaKos)
        etAlamatKos = findViewById(R.id.etAlamatKos)
        etHargaKos = findViewById(R.id.etHargaKos)
        etJumlahKamar = findViewById(R.id.etJumlahKamar)
        etKamarTersedia = findViewById(R.id.etKamarTersedia)
        etFasilitas = findViewById(R.id.etFasilitas)
        spinnerJenisKos = findViewById(R.id.spinnerJenisKos)
        btnUpdateKos = findViewById(R.id.btnUpdateKos)
        btnHapusKos = findViewById(R.id.btnHapusKos)
        val imgThumbnail = findViewById<ImageView>(R.id.imgThumbnailPreview)
        val imgKamarMandi = findViewById<ImageView>(R.id.imgKamarMandiPreview)
        val imgInterior = findViewById<ImageView>(R.id.imgInteriorPreview)
        val imgDepan = findViewById<ImageView>(R.id.imgDepanPreview)

        val baseUrl = "https://editor-mountable-dreamy.ngrok-free.dev/storage/"

        Glide.with(this).load(baseUrl + intent.getStringExtra("thumbnail")).into(imgThumbnail)
        Glide.with(this).load(baseUrl + intent.getStringExtra("gambar_kamar_mandi")).into(imgKamarMandi)
        Glide.with(this).load(baseUrl + intent.getStringExtra("gambar_interior")).into(imgInterior)
        Glide.with(this).load(baseUrl + intent.getStringExtra("gambar_depan")).into(imgDepan)

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
        val jenisLabels = arrayOf("Kos Putra", "Kos Putri", "Kos Campur")
        val jenisValues = arrayOf("putra", "putri", "campur")

        spinnerJenisKos.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            jenisLabels
        )

// Ambil data kos yang dikirim dari KosAdapter
        val namaKos = intent.getStringExtra("nama_kost")
        val alamatKos = intent.getStringExtra("alamat")
        val hargaKos = intent.getStringExtra("harga")
        val fasilitasList = intent.getStringArrayListExtra("fasilitas") ?: arrayListOf()

        val jenisKos = intent.getStringExtra("jenis_kost")?.lowercase()

        val selectedIndex = when {
            jenisKos?.contains("putra") == true -> 0
            jenisKos?.contains("putri") == true -> 1
            jenisKos?.contains("campur") == true -> 2
            else -> 0
        }


        spinnerJenisKos.setSelection(selectedIndex)

// Tampilkan data lama ke form edit
        etJumlahKamar.setText(intent.getIntExtra("jumlah_kamar", 0).toString())
        etKamarTersedia.setText(intent.getIntExtra("kamar_tersedia", 0).toString())
        etNamaKos.setText(namaKos)
        etAlamatKos.setText(alamatKos)

        etFasilitas.setText(fasilitasList.joinToString(", "))

// Bersihkan format harga dari "Rp 850000 / bulan" jadi "850000"
        etHargaKos.setText(
            hargaKos
                ?.replace("Rp ", "")
                ?.replace(" / bulan", "")
                ?.replace(".", "")
        )
        btnUpdateKos.setOnClickListener {

            val nama = etNamaKos.text.toString()
            val alamat = etAlamatKos.text.toString()
            val harga = etHargaKos.text.toString().toIntOrNull() ?: 0
            val jumlahKamar = etJumlahKamar.text.toString().toIntOrNull() ?: 0
            val kamarTersedia = etKamarTersedia.text.toString().toIntOrNull() ?: 0
            val fasilitasText = etFasilitas.text.toString()

            val fasilitasList = fasilitasText
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val fasilitasJson = fasilitasList.joinToString(
                prefix = "[\"",
                separator = "\",\"",
                postfix = "\"]"
            )

            val token = getSharedPreferences(
                "USER_SESSION",
                MODE_PRIVATE
            ).getString("token", "") ?: ""

            val mediaType = "text/plain".toMediaType()

            RetrofitClient.apiService.updateKost(
                token = "Bearer $token",
                id = kosId,
                method = "PUT".toRequestBody(mediaType),
                namaKos = nama.toRequestBody(mediaType),
                alamat = alamat.toRequestBody(mediaType),
                harga = harga.toString().toRequestBody(mediaType),
                jenisKos = jenisValues[spinnerJenisKos.selectedItemPosition].toRequestBody(mediaType),
                jumlahKamar = jumlahKamar.toString().toRequestBody(mediaType),
                kamarTersedia = kamarTersedia.toString().toRequestBody(mediaType),
                fasilitas = fasilitasJson.toRequestBody(mediaType),
                thumbnail = uriToPart(selectedThumbnail, "thumbnail"),
                gambar_kamar_mandi = uriToPart(selectedKamarMandi, "gambar_kamar_mandi"),
                gambar_interior = uriToPart(selectedInterior, "gambar_interior"),
                gambar_depan = uriToPart(selectedDepan, "gambar_depan")
            ).enqueue(object : Callback<ResponseBody> {
                // callback kamu yang sekarang


                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditKosActivity,
                            "Kos berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()

                        Toast.makeText(
                            this@EditKosActivity,
                            "Error ${response.code()}: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()

                        Log.e("UPDATE_KOS_ERROR", errorBody ?: "Tidak ada error body")
                    }
                }

                override fun onFailure(
                    call: Call<ResponseBody>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@EditKosActivity,
                        "Gagal terhubung: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }


            })
        }
        btnHapusKos.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Hapus Kos")
                .setMessage("Yakin ingin menghapus kos ini?")
                .setPositiveButton("Ya") { _, _ ->

                    deleteKos()

                }
                .setNegativeButton("Batal", null)
                .show()

        }
    }
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