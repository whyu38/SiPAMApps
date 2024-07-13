package com.wahyu.sipamapps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Lokasi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasi)

        val btnViewOnMap: Button = findViewById(R.id.btnViewOnMap)
        btnViewOnMap.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            // Url tersebut mengarahkan pada maps kantor
            intent.data = Uri.parse("https://maps.app.goo.gl/7r2rUNhDEepGfALa6")
            startActivity(intent)
        }
    }
}
