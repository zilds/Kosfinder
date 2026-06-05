package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.model.BookingRequest
import com.example.kosfinder.response.BookingResponse
import com.example.kosfinder.response.PaymentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {

    private var kostId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)


        kostId = intent.getIntExtra("KOST_ID", 0)

        val btnBackBooking = findViewById<ImageView>(R.id.btnBackBooking)
        val btnLanjutPembayaran = findViewById<Button>(R.id.btnLanjutPembayaran)

        val etNamaPenyewa = findViewById<EditText>(R.id.etNamaPenyewa)
        val etNoHpPenyewa = findViewById<EditText>(R.id.etNoHpPenyewa)
        val etEmailPenyewa = findViewById<EditText>(R.id.etEmailPenyewa)
        val etTanggalMasuk = findViewById<EditText>(R.id.etTanggalMasuk)
        val etJumlahPenghuni = findViewById<EditText>(R.id.etJumlahPenghuni)
        val etCatatan = findViewById<EditText>(R.id.etCatatan)

        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val rgDurasiSewa = findViewById<RadioGroup>(R.id.rgDurasiSewa)

        btnBackBooking.setOnClickListener {
            finish()
        }

        etTanggalMasuk.setOnClickListener {

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal Masuk")
                .build()

            datePicker.show(supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { selection ->

                val formatter = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                )

                val tanggal = formatter.format(Date(selection))

                etTanggalMasuk.setText(tanggal)
            }
        }

        btnLanjutPembayaran.setOnClickListener {
            val nama = etNamaPenyewa.text.toString().trim()
            val noHp = etNoHpPenyewa.text.toString().trim()
            val email = etEmailPenyewa.text.toString().trim()
            val tanggalMasuk = etTanggalMasuk.text.toString().trim()
            val jumlahPenghuniText = etJumlahPenghuni.text.toString().trim()
            val catatan = etCatatan.text.toString().trim()

            val gender = when (rgGender.checkedRadioButtonId) {
                R.id.rbLaki -> "laki-laki"
                R.id.rbPerempuan -> "perempuan"
                else -> "laki-laki"
            }

            val durasiBulan = when (rgDurasiSewa.checkedRadioButtonId) {
                R.id.rb1Bulan -> 1
                R.id.rb3Bulan -> 3
                R.id.rbTahunan -> 12
                else -> 1
            }

            if (kostId == 0) {
                Toast.makeText(this, "Data kos tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nama.isEmpty() || noHp.isEmpty() || tanggalMasuk.isEmpty() || jumlahPenghuniText.isEmpty()) {
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = BookingRequest(
                kost_id = kostId,
                nama_penyewa = nama,
                no_hp = noHp,
                email = if (email.isEmpty()) null else email,
                jenis_kelamin = gender,
                tanggal_masuk = tanggalMasuk,
                durasi_sewa = durasiBulan,
                jumlah_penghuni = jumlahPenghuniText.toInt(),
                catatan = if (catatan.isEmpty()) null else catatan
            )

            val token = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                .getString("token", "")

            RetrofitClient.apiService.createBooking("Bearer $token", request)
                .enqueue(object : Callback<BookingResponse> {
                    override fun onResponse(
                        call: Call<BookingResponse>,
                        response: Response<BookingResponse>
                    ) {
                        if (response.isSuccessful) {
                            val bookingId = response.body()!!.booking.id

                            createPayment(bookingId)
                        } else {
                            Toast.makeText(
                                this@BookingActivity,
                                "Gagal booking: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                        Toast.makeText(
                            this@BookingActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
    private fun createPayment(bookingId: Int) {
        val token = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            .getString("token", "")

        RetrofitClient.apiService.createPayment("Bearer $token", bookingId)
            .enqueue(object : Callback<PaymentResponse> {
                override fun onResponse(
                    call: Call<PaymentResponse>,
                    response: Response<PaymentResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val snapToken = response.body()!!.snap_token

                        val intent =
                            Intent(this@BookingActivity, PaymentWebViewActivity::class.java)
                        intent.putExtra("SNAP_TOKEN", snapToken)
                        startActivity(intent)

                        // nanti snapToken ini dipakai untuk buka Midtrans
                    } else {
                        Toast.makeText(
                            this@BookingActivity,
                            "Gagal membuat pembayaran",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                    Toast.makeText(
                        this@BookingActivity,
                        "Error payment: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}