package com.wahyu.sipamapps

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Cek : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cek)

        val editNamaPelanggan: EditText = findViewById(R.id.editNamaPelanggan)
        val editNomorMeter: EditText = findViewById(R.id.editNomorMeter)
        val editJumlahPenggunaan: EditText = findViewById(R.id.editJumlahPenggunaan)
        val editTanggalPembayaran: EditText = findViewById(R.id.editTanggalPembayaran)
        val radioGroupKelas: RadioGroup = findViewById(R.id.radioGroupKelas)
        val btnCekTagihan: Button = findViewById(R.id.btnCekTagihan)

        // Set current date to Tanggal Pembayaran
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        editTanggalPembayaran.setText(currentDate)

        btnCekTagihan.setOnClickListener {
            val namaPelanggan = editNamaPelanggan.text.toString()
            val nomorMeter = editNomorMeter.text.toString()
            val jumlahPenggunaan = editJumlahPenggunaan.text.toString()
            val tanggalPembayaran = editTanggalPembayaran.text.toString()
            val selectedKelasId = radioGroupKelas.checkedRadioButtonId

            // Input validation
            if (namaPelanggan.isEmpty() || nomorMeter.isEmpty() || jumlahPenggunaan.isEmpty() || selectedKelasId == -1) {
                Toast.makeText(this, "Lengkapi semua form!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val penggunaan = jumlahPenggunaan.toIntOrNull()
            if (penggunaan == null || penggunaan <= 0) {
                Toast.makeText(this, "Isi angka yang valid pada Jumlah Penggunaan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val biayaPerM3 = when (selectedKelasId) {
                R.id.radioKelas1 -> 3500
                R.id.radioKelas2 -> 3000
                R.id.radioKelas3 -> 2500
                else -> 0
            }

            val biayaAdmin = 2500
            val totalTagihan = penggunaan * biayaPerM3 + biayaAdmin

            val intent = Intent(this, DetailCek::class.java).apply {
                putExtra("namaPelanggan", namaPelanggan)
                putExtra("nomorMeter", nomorMeter)
                putExtra("jumlahPenggunaan", jumlahPenggunaan)
                putExtra("tanggalPembayaran", tanggalPembayaran)
                putExtra("kelas", when (selectedKelasId) {
                    R.id.radioKelas1 -> "I"
                    R.id.radioKelas2 -> "II"
                    R.id.radioKelas3 -> "III"
                    else -> ""
                })
                putExtra("totalTagihan", totalTagihan.toString())
                putExtra("biayaAdmin", biayaAdmin.toString())
                putExtra("denda", "Rp. 10,000")
            }
            startActivity(intent)
        }
    }
}
