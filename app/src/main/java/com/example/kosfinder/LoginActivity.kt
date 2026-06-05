package com.example.kosfinder

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.model.LoginRequest
import com.example.kosfinder.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Menghubungkan komponen XML ke Kotlin
        etEmail = findViewById(R.id.etEmailLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        txtToRegister = findViewById(R.id.txtToRegister)

        // Saat tombol login diklik, jalankan function login()
        btnLogin.setOnClickListener {
            login()
        }

        // Pindah ke halaman register
        txtToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        // Ambil input dari EditText
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validasi sederhana
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this,
                "Email dan password wajib diisi",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Membuat request body untuk dikirim ke Laravel
        val request = LoginRequest(email, password)

        // Mengirim request login ke API Laravel
        RetrofitClient.apiService.login(request)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val data = response.body()!!

                        // Menyimpan data login ke SharedPreferences
                        // Supaya user tetap dianggap login walaupun app ditutup
                        val pref: SharedPreferences =
                            getSharedPreferences("USER_SESSION", MODE_PRIVATE)

                        pref.edit()
                            .putString("token", data.token)
                            .putString("name", data.user.name)
                            .putString("email", data.user.email)
                            .putString("phone", data.user.phone ?: "-")
                            .putString("role", data.user.role)
                            .apply()

                        Toast.makeText(
                            this@LoginActivity,
                            data.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        // Cek role user dari response Laravel
                        // user  -> masuk HomeActivity
                        // owner -> masuk OwnerAddKosActivity
                        val tujuan = when (data.user.role) {
                            "owner" -> OwnerKosActivity::class.java
                            "user" -> HomeActivity::class.java
                            else -> HomeActivity::class.java
                        }

                        val intent = Intent(this@LoginActivity, tujuan)

                        // Membersihkan halaman sebelumnya agar tidak bisa back ke login
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<LoginResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Gagal koneksi: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}