package com.wahyu.sipamapps

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "login_pref"
    private val IS_LOGGED_IN = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        // Pengecekan login di onCreate
        checkLoginStatus()

        val btnCheckBill: ImageButton = findViewById(R.id.btn_check_bill)
        val btnNewConnection: ImageButton = findViewById(R.id.btn_new_connection)
        val btnComplaint: ImageButton = findViewById(R.id.btn_complaint)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // List gambar slider
        val images = listOf(
            R.drawable.s1,
            R.drawable.s2,
            R.drawable.s3
        )

        // Adapter slider
        val adapter = ImageSliderAdapter(images)
        viewPager.adapter = adapter

        btnCheckBill.setOnClickListener {
            startActivity(Intent(this, Cek::class.java))
        }

        btnNewConnection.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
                startActivity(Intent(this, Pasang::class.java))
        }

        btnComplaint.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send/?phone=6289636860431&text&type=phone_number&app_absent=0")
            startActivity(intent)
        }

        // Listener untuk BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, Home::class.java))
                    true
                }
                R.id.nav_activity -> {
                    startActivity(Intent(this, Riwayat::class.java))
                    true
                }
                R.id.nav_account -> {
                    startActivity(Intent(this, Akun::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Pengecekan login setiap kali layar diaktifkan
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        if (!sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            goToLoginActivity()
        }
    }

    private fun logout() {
        val databaseHelper = DatabaseHelper(this)
        databaseHelper.clearAllRiwayat()

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

    fun openArticleLink(view: View) {
        var url = ""

        when (view.id) {
            R.id.infoItem1 -> url = "https://radarmalang.jawapos.com/berita-terbaru/811454151/lagi-perumda-tirta-kanjuruhan-sabet-innovative-government-award"
            R.id.infoItem2 -> url = "https://pdaminfo.pdampintar.id/blog/lainnya/ini-jumlah-pemakaian-normal-air-rumah-tangga#google_vignette"
            R.id.infoItem3 -> url = "https://radarmalang.jawapos.com/kabupaten-malang/811089159/torehan-prestasi-bergengsi-perumda-tirta-kanjuruhan"
            R.id.infoItem4 -> url = "https://www.99.co/id/panduan/cara-menghitung-tarif-pdam/"
            R.id.infoItem5 -> url = "https://www.perumdamtkr.com/read/65faca8d-3e80-4045-9d35-4297a747c35f/berita/pertahankan-prestasi-pdam-kabupaten-tangerang-jadi"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}