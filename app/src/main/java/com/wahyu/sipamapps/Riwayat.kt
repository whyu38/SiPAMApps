package com.wahyu.sipamapps

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Riwayat : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var linearLayoutRiwayat: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        databaseHelper = DatabaseHelper(this)
        linearLayoutRiwayat = findViewById(R.id.linearLayoutRiwayat)

        val riwayatList = databaseHelper.getAllRiwayat()
        displayRiwayat(riwayatList)
    }

    private fun displayRiwayat(riwayatList: List<RiwayatPembayaran>) {
        for (riwayat in riwayatList) {
            val textView = TextView(this)
            val formattedJumlah = "Rp. ${riwayat.jumlah}"
            textView.text = "${riwayat.tanggal} - $formattedJumlah - ${riwayat.status}"
            textView.textSize = 16f
            textView.setPadding(8, 8, 8, 8)
            linearLayoutRiwayat.addView(textView)
        }
    }
}
