package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kosfinder.api.RetrofitClient
import com.example.kosfinder.model.RegisterRequest
import com.example.kosfinder.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter
import android.widget.Spinner

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etNameRegister)
        val etEmail = findViewById<EditText>(R.id.etEmailRegister)
        val etPhone = findViewById<EditText>(R.id.etPhoneRegister)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnToLogin = findViewById<TextView>(R.id.txtToLogin)

        val spinnerRole = findViewById<Spinner>(R.id.spinnerRoleRegister)

        val roleLabels = arrayOf("Mencari Kos", "Pemilik Kos")
        val roleValues = arrayOf("user", "owner")

        val roleAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roleLabels
        )

        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = roleAdapter

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()


            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.length < 11) {
                Toast.makeText(this, "Nomor HP minimal 11 angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedRole = roleValues[spinnerRole.selectedItemPosition]
            val request = RegisterRequest(name, email, phone, password, selectedRole
            )

            RetrofitClient.apiService.register(request)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!!
                            Toast.makeText(
                                this@RegisterActivity,
                                "${data.message}, silakan login",
                                Toast.LENGTH_LONG
                            ).show()

                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(this@RegisterActivity, "Register gagal. Email mungkin sudah dipakai.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }

        btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}