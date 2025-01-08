package com.wahyu.sipamapps

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.wahyu.sipamapps.viewmodel.NetworkViewModel
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private val networkViewModel: NetworkViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var biometricExecutor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    private val PREF_NAME = "login_pref"
    private val IS_LOGGED_IN = "isLoggedIn"
    private val BIOMETRIC_ENABLED = "biometricEnabled"

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        networkViewModel.isConnected.observe(this, Observer { isConnected ->
            if (!isConnected) {
                showNoInternetDialog()
            }
        })

        // Periksa status login
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            goToHomeActivity()
        }

        // Setup UI
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnBiometricLogin = findViewById<Button>(R.id.btnBiometricLogin)

        // Setup Login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email == "user@sipam.my.id" && password == "password") {
                val editor = sharedPreferences.edit()
                editor.putBoolean(IS_LOGGED_IN, true)
                editor.putBoolean(BIOMETRIC_ENABLED, true) // Simpan status biometrik
                editor.apply()

                goToHomeActivity()
            } else {
                Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show()
            }
        }

        // Periksa dukungan biometrik
        if (isBiometricSupported()) {
            setupBiometricLogin()
            btnBiometricLogin.isEnabled = true
        } else {
            btnBiometricLogin.isEnabled = false
        }

        btnBiometricLogin.setOnClickListener {
            biometricPrompt.authenticate(createBiometricPromptInfo())
        }
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "Perangkat tidak mendukung biometrik.", Toast.LENGTH_SHORT).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Hardware biometrik tidak tersedia.", Toast.LENGTH_SHORT).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "Tidak ada sidik jari yang terdaftar.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> false
        }
    }

    private fun setupBiometricLogin() {
        biometricExecutor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, biometricExecutor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Setelah autentikasi berhasil, simpan status login dan arahkan ke HomeActivity
                val editor = sharedPreferences.edit()
                editor.putBoolean(IS_LOGGED_IN, true)
                editor.apply()

                goToHomeActivity()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@MainActivity, "Autentikasi gagal, coba lagi.", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@MainActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login Biometrik")
            .setSubtitle("Gunakan sidik jari untuk login")
            .setDescription("Pastikan sidik jari sudah terdaftar di perangkat Anda.")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("Koneksi Internet")
            .setMessage("Tidak ada koneksi internet. Silahkan periksa koneksi Anda dan coba lagi.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish() // Tutup MainActivity setelah navigasi ke Home
    }
}
