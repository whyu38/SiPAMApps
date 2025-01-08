package com.wahyu.sipamapps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class DetailCek : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_cek)

        databaseHelper = DatabaseHelper(this)

        val tvNamaPelanggan: TextView = findViewById(R.id.tvNamaPelanggan)
        val tvNomorMeter: TextView = findViewById(R.id.tvNomorMeter)
        val tvJumlahPenggunaan: TextView = findViewById(R.id.tvJumlahPenggunaan)
        val tvTanggalPembayaran: TextView = findViewById(R.id.tvTanggalPembayaran)
        val tvKelas: TextView = findViewById(R.id.tvKelas)
        val tvDenda: TextView = findViewById(R.id.tvDenda)
        val tvBiayaAdmin: TextView = findViewById(R.id.tvBiayaAdmin)
        val tvTotalTagihan: TextView = findViewById(R.id.tvTotal)

        // Mendapatkan nilai dari intent
        val namaPelanggan = intent.getStringExtra("namaPelanggan")
        val nomorMeter = intent.getStringExtra("nomorMeter")
        val jumlahPenggunaan = intent.getStringExtra("jumlahPenggunaan")
        val tanggalPembayaran = intent.getStringExtra("tanggalPembayaran")
        val kelas = intent.getStringExtra("kelas")
        val biayaAdmin = intent.getStringExtra("biayaAdmin")
        val totalTagihan = intent.getStringExtra("totalTagihan")

        // Mengatur teks pada TextView dengan format yang diminta
        tvNamaPelanggan.text = "Nama Pelanggan: $namaPelanggan"
        tvNomorMeter.text = "Nomor Meter: $nomorMeter"
        tvJumlahPenggunaan.text = "Jumlah Penggunaan: $jumlahPenggunaan m3"
        tvTanggalPembayaran.text = "Tanggal Pembayaran: $tanggalPembayaran"
        tvKelas.text = "Kelas: $kelas"
        tvBiayaAdmin.text = "Biaya Admin: Rp. $biayaAdmin"
        tvTotalTagihan.text = "Total Tagihan: Rp. $totalTagihan"

        val btnBayarSekarang: Button = findViewById(R.id.btnBayarSekarang)
        btnBayarSekarang.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

            // Cek apakah sudah tanggal 10 atau lebih
            if (dayOfMonth > 10) {
                val totalTagihanInt = totalTagihan?.toIntOrNull() ?: 0
                val totalDenganDenda = totalTagihanInt + 10000 // Denda Rp. 10.000
                val status = "Lunas dengan denda"

                // Simpan riwayat pembayaran
                val result = databaseHelper.addRiwayat(tanggalPembayaran!!, totalDenganDenda.toString(), status)

                if (result != -1L) {
                    Toast.makeText(this, "Pembayaran berhasil disimpan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Payment::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Pembayaran gagal disimpan", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Tidak ada denda karena pembayaran dilakukan sebelum atau tanggal 10
                val status = "Lunas tanpa denda"

                // Simpan riwayat pembayaran
                val result = databaseHelper.addRiwayat(tanggalPembayaran!!, totalTagihan!!, status)

                if (result != -1L) {
                    Toast.makeText(this, "Pembayaran berhasil disimpan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Payment::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Pembayaran gagal disimpan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}