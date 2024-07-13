package com.wahyu.sipamapps

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.wahyu.sipamapps.viewmodel.NetworkViewModel

class MainActivity : AppCompatActivity() {

    private val networkViewModel: NetworkViewModel by viewModels()
    lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "login_pref"
    private val IS_LOGGED_IN = "isLoggedIn"

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

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            networkViewModel.isConnected.observe(this, Observer { isConnected ->
                if (isConnected) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    if (email == "user@sipam.my.id" && password == "password") {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean(IS_LOGGED_IN, true)
                        editor.apply()

                        goToHomeActivity()
                    } else {
                        Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Tidak ada koneksi internet, Periksa koneksi anda dan coba lagi.", Toast.LENGTH_SHORT).show()
                }
            })
        }
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
        finish()
    }
}
