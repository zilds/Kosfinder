package com.example.kosfinder

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentWebViewActivity : AppCompatActivity() {

    private lateinit var webViewPayment: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webViewPayment = WebView(this)
        setContentView(webViewPayment)

        val snapToken = intent.getStringExtra("SNAP_TOKEN")

        if (snapToken.isNullOrEmpty()) {
            Toast.makeText(this, "Snap token tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        webViewPayment.settings.javaScriptEnabled = true
        webViewPayment.settings.domStorageEnabled = true

        webViewPayment.webViewClient = WebViewClient()

        val paymentUrl = "https://app.sandbox.midtrans.com/snap/v2/vtweb/$snapToken"

        webViewPayment.loadUrl(paymentUrl)
    }
}