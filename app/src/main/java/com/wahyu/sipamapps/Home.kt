package com.wahyu.sipamapps

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class Home : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "login_pref"
    private val IS_LOGGED_IN = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        if (!sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            goToLoginActivity()
        }

        val btnInfo: ImageButton = findViewById(R.id.info_button)
        val btnCheckBill: ImageButton = findViewById(R.id.btn_check_bill)
        val btnHistory: ImageButton = findViewById(R.id.btn_history)
        val btnNewConnection: ImageButton = findViewById(R.id.btn_new_connection)
        val btnComplaint: ImageButton = findViewById(R.id.btn_complaint)
        val btnOffice: ImageButton = findViewById(R.id.btn_office)
        val btnLogout: ImageButton = findViewById(R.id.btn_logout)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        // List gambar slider
        val images = listOf(
            R.drawable.s1,
            R.drawable.s2,
            R.drawable.s3
        )

        // adapter Slider
        val adapter = ImageSliderAdapter(images)
        viewPager.adapter = adapter

        btnInfo.setOnClickListener {
            startActivity(Intent(this, About::class.java))
        }

        btnCheckBill.setOnClickListener {
            startActivity(Intent(this, Cek::class.java))
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, Riwayat::class.java))
        }

        btnNewConnection.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            // URL untuk menghubungi customer service
            intent.data = Uri.parse("https://api.whatsapp.com/send/?phone=6289636860431&text&type=phone_number&app_absent=0")
            startActivity(intent)
        }

        btnComplaint.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            // URL untuk menghubungi customer service
            intent.data = Uri.parse("https://api.whatsapp.com/send/?phone=6289636860431&text&type=phone_number&app_absent=0")
            startActivity(intent)
        }

        btnOffice.setOnClickListener {
            startActivity(Intent(this, Lokasi::class.java))
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val databaseHelper = DatabaseHelper(this)
        databaseHelper.clearAllRiwayat() // Menghapus semua riwayat pembayaran dari database

        val editor = sharedPreferences.edit()
        editor.remove(IS_LOGGED_IN)
        editor.apply()

        goToLoginActivity()
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Metode untuk membuka tautan artikel
    fun openArticleLink(view: View) {
        var url = "" // URL sesuai dengan artikel yang diklik

        // Menentukan URL berdasarkan id view yang diklik
        when (view.id) {
            R.id.infoItem1 -> url = "https://radarmalang.jawapos.com/berita-terbaru/811454151/lagi-perumda-tirta-kanjuruhan-sabet-innovative-government-award"
            R.id.infoItem2 -> url = "https://pdaminfo.pdampintar.id/blog/lainnya/ini-jumlah-pemakaian-normal-air-rumah-tangga#google_vignette"
            R.id.infoItem3 -> url = "https://radarmalang.jawapos.com/kabupaten-malang/811089159/torehan-prestasi-bergengsi-perumda-tirta-kanjuruhan"
            R.id.infoItem4 -> url = "https://www.99.co/id/panduan/cara-menghitung-tarif-pdam/"
            R.id.infoItem5 -> url = "https://www.perumdamtkr.com/read/65faca8d-3e80-4045-9d35-4297a747c35f/berita/pertahankan-prestasi-pdam-kabupaten-tangerang-jadi"
        }

        // Buka tautan artikel di browser
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
