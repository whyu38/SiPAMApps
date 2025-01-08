package com.wahyu.sipamapps

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.biometric.BiometricPrompt
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.Executor

class Akun : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "login_pref"
    private val IS_LOGGED_IN = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        // Bottom Navigation setup
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
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
                R.id.nav_account -> true // Tetap di halaman ini
                else -> false
            }
        }

        // ImageButton Actions
        val btnEditProfile: ImageButton = findViewById(R.id.btn_edit_profile)
        val btnOffice: ImageButton = findViewById(R.id.office)
        val btnInfo: ImageButton = findViewById(R.id.info)
        val btnHelp: ImageButton = findViewById(R.id.help)
        val btnBiometric: ImageButton = findViewById(R.id.biometric)
        val btnPrivacy: ImageButton = findViewById(R.id.privacy)
        val btnTerms: ImageButton = findViewById(R.id.kebijakan)
        val btnRate: ImageButton = findViewById(R.id.rating)
        val btnLogout: ImageButton = findViewById(R.id.logout)

        // Navigasi ke Edit Profile
        btnEditProfile.setOnClickListener {
            // Tambahkan navigasi ke Edit Profile
        }

        // Navigasi ke Lokasi Office
        btnOffice.setOnClickListener {
            startActivity(Intent(this, Lokasi::class.java))
        }

        // Navigasi ke Informasi
        btnInfo.setOnClickListener {
            startActivity(Intent(this, About::class.java))
        }

        // Navigasi ke Help
        btnHelp.setOnClickListener {
            startActivity(Intent(this, Help::class.java))
        }

        // Registrasi Biometrik
        btnBiometric.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                showBiometricPromptForEnrollment()
            } else {
                println("Perangkat tidak mendukung pendaftaran biometrik.")
            }
        }

        // Navigasi ke Kebijakan Privasi
        btnPrivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicy::class.java))
        }

        // Navigasi ke Syarat & Ketentuan
        btnTerms.setOnClickListener {
            startActivity(Intent(this, Terms::class.java))
        }

        // Navigasi ke Rate App
        btnRate.setOnClickListener {
            // Tambahkan fungsi rate app
        }

        // Logout Action
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Hanya update status login tanpa menghapus data biometrik
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGGED_IN, false) // Tandai pengguna sudah logout
        editor.apply()

        // Navigasi kembali ke MainActivity (halaman login)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Tutup Akun Activity sepenuhnya
    }

    private fun showBiometricPromptForEnrollment() {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    saveBiometricEnrollment(true)
                    println("Pendaftaran biometrik berhasil.")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    println("Pendaftaran biometrik gagal. Coba lagi.")
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    println("Error saat pendaftaran biometrik: $errString")
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Pendaftaran Biometrik")
            .setSubtitle("Daftar menggunakan sidik jari Anda")
            .setDescription("Letakkan jari Anda di sensor untuk mendaftar")
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun saveBiometricEnrollment(isEnrolled: Boolean) {
        val biometricPrefs = getSharedPreferences("BiometricPrefs", MODE_PRIVATE)
        val editor = biometricPrefs.edit()
        editor.putBoolean("isBiometricEnrolled", isEnrolled)
        editor.apply()
    }
}
