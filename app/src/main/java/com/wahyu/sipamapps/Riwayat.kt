package com.wahyu.sipamapps

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Riwayat : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var linearLayoutRiwayat: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        databaseHelper = DatabaseHelper(this)
        linearLayoutRiwayat = findViewById(R.id.linearLayoutRiwayat)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Mendengarkan klik item pada BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, Home::class.java))
                    true
                }
                R.id.nav_activity -> true

                R.id.nav_account -> {
                    startActivity(Intent(this, Akun::class.java))
                    true
                }
                else -> false
            }
        }

        // Menampilkan Riwayat Pembayaran dan Pesanan Baru
        displayRiwayatPembayaran()
        displayPesananBaru()
    }

    private fun displayRiwayatPembayaran() {
        val riwayatList = databaseHelper.getAllRiwayat()

        if (riwayatList.isNotEmpty()) {
            val headerTextView = createHeaderTextView("Riwayat Pembayaran")
            linearLayoutRiwayat.addView(headerTextView)

            riwayatList.forEach { riwayat ->
                val riwayatTextView = createTextView("${riwayat.tanggal} - Rp. ${riwayat.jumlah} - ${riwayat.status}")
                linearLayoutRiwayat.addView(riwayatTextView)
            }
        }
    }

    private fun displayPesananBaru() {
        val pesananList = databaseHelper.getAllOrders()

        if (pesananList.isNotEmpty()) {
            val headerTextView = createHeaderTextView("\nPesanan Baru")
            linearLayoutRiwayat.addView(headerTextView)

            pesananList.forEach { pesanan ->
                val pesananTextView = createTextView("""
                    Nama: ${pesanan.nama ?: "Tidak tersedia"}
                    Lokasi: (${pesanan.latitude ?: "Tidak tersedia"}, ${pesanan.longitude ?: "Tidak tersedia"})
                    Kelas: ${pesanan.kelas ?: "Tidak tersedia"}
                    Alamat: ${pesanan.alamat ?: "Tidak tersedia"}
                """.trimIndent())
                linearLayoutRiwayat.addView(pesananTextView)
            }
        }
    }

    private fun createHeaderTextView(header: String): TextView {
        return TextView(this).apply {
            text = header
            textSize = 18f
            setPadding(8, 16, 8, 8)
        }
    }

    private fun createTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 16f
            setPadding(8, 8, 8, 8)
        }
    }
}
