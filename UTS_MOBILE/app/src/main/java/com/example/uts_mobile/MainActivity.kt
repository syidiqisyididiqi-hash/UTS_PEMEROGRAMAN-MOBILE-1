package com.example.uts_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)
        val usernameText = findViewById<TextView>(R.id.tvUsername)
        usernameText.text = if (!username.isNullOrBlank()) {
            getString(R.string.main_username_format, username)
        } else {
            getString(R.string.main_username_default)
        }

        usernameText.visibility = if (username.isNullOrBlank()) View.GONE else View.VISIBLE

        val daftarSeminarButton = findViewById<Button>(R.id.btnDaftarSeminar)
        daftarSeminarButton.setOnClickListener {
            startActivity(Intent(this, SeminarRegistrationActivity::class.java))
        }
    }
}