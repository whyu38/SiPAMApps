package com.wahyu.sipamapps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val btnConfirmPayment: Button = findViewById(R.id.btnConfirmPayment)
        btnConfirmPayment.setOnClickListener {
            // Intent untuk membuka halaman form pengunggahan bukti pembayaran non-QRIS
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/H8o3zA7JuAKbHCmMA"))
            startActivity(intent)
        }

        val btnBack: Button = findViewById(R.id.btnBacktoHome)
        btnBack.setOnClickListener {
            // Intent untuk kembali ke halaman Home
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish() // Optional: menutup activity saat kembali ke Home
        }
    }
}
