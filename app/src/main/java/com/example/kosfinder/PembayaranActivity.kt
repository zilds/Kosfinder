package com.example.kosfinder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PembayaranActivity : AppCompatActivity() {

    // Variabel global untuk menyimpan data gambar dari galeri HP
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        // 1. INISIALISASI KOMPONEN UI (Sesuai ID di layout baru)
        val btnBackPembayaran = findViewById<ImageView>(R.id.btnBackPembayaran)
        val txtTotal = findViewById<TextView>(R.id.txtTotalBayar) // Menampilkan harga sewa otomatis
        val rgMetodeBayar = findViewById<RadioGroup>(R.id.rgMetodeBayar)

        // Komponen Detail Rekening Dinamis
        val layoutDetailRekening = findViewById<LinearLayout>(R.id.layoutDetailRekening)
        val txtLabelMetodeTerpilih = findViewById<TextView>(R.id.txtLabelMetodeTerpilih)
        val txtNomorRekening = findViewById<TextView>(R.id.txtNomorRekening)
        val txtNamaPemilik = findViewById<TextView>(R.id.txtNamaPemilik)

        // Tombol Aksi
        val btnUploadBukti = findViewById<Button>(R.id.btnUploadBukti)
        val btnKonfirmasiPembayaran = findViewById<Button>(R.id.btnKonfirmasiPembayaran)

        // 2. TANGKAP DATA DARI FORM PENYEWA SEBELUMNYA (Fungsi Lama Dipertahankan)
        val namaPenyewa = intent.getStringExtra("EXTRA_NAMA") ?: "Penyewa"
        val hargaTotal = intent.getStringExtra("EXTRA_HARGA") ?: "Rp 850.000 / bln"

        // Set teks harga total dari kiriman halaman sebelumnya
        txtTotal.text = hargaTotal

        // 3. FUNGSI UNTUK MEMBUKA GALERI HP ASLI (Pengganti Simulasi Toast)
        val getImageGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                // Ubah teks tombol sebagai penanda file masuk
                btnUploadBukti.text = "✅ Bukti Transfer Berhasil Dipilih!"
                Toast.makeText(this, "Gambar berhasil dimuat ke sistem!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Back / Kembali
        btnBackPembayaran.setOnClickListener { finish() }

        // Tombol Upload Bukti Transfer (Membuka Galeri Asli)
        btnUploadBukti.setOnClickListener {
            getImageGallery.launch("image/*")
        }

        // 4. LOGIKA MENAMPILKAN DETAIL NOMOR REKENING SECARA DINAMIS
        rgMetodeBayar.setOnCheckedChangeListener { _, checkedId ->
            // Munculkan container box detail info rekening
            layoutDetailRekening.visibility = View.VISIBLE

            when (checkedId) {
                R.id.rbTransferBank -> {
                    txtLabelMetodeTerpilih.text = "Tujuan Transfer Bank (BCA):"
                    txtNomorRekening.text = "024-12345-67"
                    txtNamaPemilik.text = "a.n. Pemilik Kosfinder Berkah"
                }
                R.id.rbEWallet -> {
                    txtLabelMetodeTerpilih.text = "Tujuan Transfer E-Wallet (DANA):"
                    txtNomorRekening.text = "0812-3456-7890"
                    txtNamaPemilik.text = "a.n. KOSFINDER OFFICIAL"
                }
                R.id.rbBayarTempat -> {
                    txtLabelMetodeTerpilih.text = "Ketentuan Bayar di Tempat (COD):"
                    txtNomorRekening.text = "Silakan siapkan uang tunai pas"
                    txtNamaPemilik.text = "Bayar langsung ke pengelola saat tiba di lokasi kos."
                }
                else -> {
                    layoutDetailRekening.visibility = View.GONE
                }
            }
        }

        // 5. TOMBOL KONFIRMASI AKHIR & OPER DATA (Fungsi Lama + Validasi Keamanan)
        btnKonfirmasiPembayaran.setOnClickListener {
            val selectedMethodId = rgMetodeBayar.checkedRadioButtonId

            // Validasi 1: Pastikan user sudah memilih salah satu metode
            if (selectedMethodId == -1) {
                Toast.makeText(this, "Silakan pilih metode pembayaran dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi 2: Jika milih Transfer/E-Wallet, wajib melampirkan gambar bukti transfer dari galeri
            if ((selectedMethodId == R.id.rbTransferBank || selectedMethodId == R.id.rbEWallet) && imageUri == null) {
                Toast.makeText(this, "Harap upload foto bukti pembayaran Anda!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // JIKA LOLOS VALIDASI -> Pindah ke Halaman Akhir & Kirim Data Nama Penyewa (Fungsi Lama)
            val intentStatus = Intent(this, StatusPesananActivity::class.java)
            intentStatus.putExtra("EXTRA_NAMA", namaPenyewa)
            startActivity(intentStatus)

            // Selesai, tutup halaman pembayaran agar user tidak bisa 'Back' ke form bayar lagi
            finish()
        }
    }
}