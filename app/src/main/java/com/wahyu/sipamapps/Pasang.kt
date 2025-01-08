package com.wahyu.sipamapps

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Pasang : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextLatitude: EditText
    private lateinit var editTextLongitude: EditText
    private lateinit var buttonFindAddress: Button
    private lateinit var buttonPlaceOrder: Button
    private lateinit var textViewAddress: TextView
    private lateinit var radioGroupClass: RadioGroup
    private lateinit var radioButtonClassI: RadioButton
    private lateinit var radioButtonClassII: RadioButton
    private lateinit var radioButtonClassIII: RadioButton

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasang)

        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Initialize views
        editTextName = findViewById(R.id.editTextName)
        editTextLatitude = findViewById(R.id.editTextLatitude)
        editTextLongitude = findViewById(R.id.editTextLongitude)
        buttonFindAddress = findViewById(R.id.buttonFindAddress)
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder)
        textViewAddress = findViewById(R.id.textViewAddress)
        radioGroupClass = findViewById(R.id.radioGroupClass)
        radioButtonClassI = findViewById(R.id.radioButtonI)
        radioButtonClassII = findViewById(R.id.radioButtonII)
        radioButtonClassIII = findViewById(R.id.radioButtonIII)

        // Handle "Find Address" button click
        buttonFindAddress.setOnClickListener {
            val latitude = editTextLatitude.text.toString().toDoubleOrNull()
            val longitude = editTextLongitude.text.toString().toDoubleOrNull()

            if (latitude != null && longitude != null) {
                val address = getAddressFromLatLong(latitude, longitude)
                if (address != null) {
                    textViewAddress.text = "Alamat Anda: $address"
                } else {
                    textViewAddress.text = "Alamat tidak ditemukan."
                }
            } else {
                textViewAddress.text = "Harap masukkan latitude dan longitude yang valid."
            }
        }

        // Handle "Place Order" button click
        buttonPlaceOrder.setOnClickListener {
            val name = editTextName.text.toString()
            val latitude = editTextLatitude.text.toString()
            val longitude = editTextLongitude.text.toString()
            val address = textViewAddress.text.toString().removePrefix("Alamat Anda: ").trim()

            if (name.isEmpty() || latitude.isEmpty() || longitude.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Harap isi semua bidang dengan benar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get selected class
            val selectedClassId = radioGroupClass.checkedRadioButtonId
            val customerClass: String = when (selectedClassId) {
                R.id.radioButtonI -> "I"
                R.id.radioButtonII -> "II"
                R.id.radioButtonIII -> "III"
                else -> "Tidak dipilih"
            }

            // Save order to database
            val result = databaseHelper.addOrder(name, latitude, longitude, customerClass, address)
            if (result != -1L) {
                Toast.makeText(this, "Pesanan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Toast.makeText(this, "Gagal menyimpan pesanan.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to get address from latitude and longitude
    private fun getAddressFromLatLong(lat: Double, long: Double): String? {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0) // Full address as a string
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Function to clear input fields
    private fun clearFields() {
        editTextName.text.clear()
        editTextLatitude.text.clear()
        editTextLongitude.text.clear()
        textViewAddress.text = ""
        radioGroupClass.clearCheck()
    }
}
