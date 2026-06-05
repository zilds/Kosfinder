package com.example.kosfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileOwnerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val token = pref.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Silakan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_profile_owner)

        val txtNamaUser = findViewById<TextView>(R.id.txtNamaUser)
        val txtEmailUser = findViewById<TextView>(R.id.txtEmailUser)
        val txtPhoneUser = findViewById<TextView>(R.id.txtPhoneUser)

        txtNamaUser.text = pref.getString("name", "Nama User")
        txtEmailUser.text = pref.getString("email", "Email User")
        txtPhoneUser.text = pref.getString("phone", "Phone User")



        val btnMenuKeluar = findViewById<Button>(R.id.btnMenuKeluar)
        btnMenuKeluar.setOnClickListener {
            pref.edit().clear().apply()

            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        val btnBackProfile = findViewById<ImageView>(R.id.btnBackProfile)
        btnBackProfile.setOnClickListener {
            finish()
        }
    }
}