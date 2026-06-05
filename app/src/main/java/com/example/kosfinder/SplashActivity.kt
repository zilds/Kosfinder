package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menampilkan layout splash screen
        setContentView(R.layout.activity_splash)

        // Delay 3 detik untuk menampilkan logo / opening
        Handler(Looper.getMainLooper()).postDelayed({

            // Mengambil data login yang tersimpan
            val pref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)

            // Mengambil token login
            val token = pref.getString("token", null)

            // Mengambil role user
            val role = pref.getString("role", null)

            // Menentukan halaman tujuan
            val tujuan = if (!token.isNullOrEmpty()) {

                // Jika user masih login

                if (role == "owner") {

                    // Jika role owner masuk ke halaman owner
                    OwnerKosActivity::class.java

                } else {

                    // Jika role user masuk ke homepage
                    HomeActivity::class.java
                }

            } else {

                // Jika belum login masuk ke halaman login
                LoginActivity::class.java
            }

            // Pindah ke halaman yang sudah ditentukan
            startActivity(Intent(this, tujuan))

            // Menutup SplashActivity agar tidak bisa kembali dengan tombol back
            finish()

        }, 3000) // 3000 ms = 3 detik
    }
}