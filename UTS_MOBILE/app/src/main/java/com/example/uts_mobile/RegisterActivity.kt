package com.example.uts_mobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usernameInput = findViewById<EditText>(R.id.etRegisterUsername)
        val passwordInput = findViewById<EditText>(R.id.etRegisterPassword)
        val confirmInput = findViewById<EditText>(R.id.etRegisterConfirmPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val backToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirm = confirmInput.text.toString()

            if (username.isBlank() || password.isBlank() || confirm.isBlank()) {
                Toast.makeText(this, "Semua field wajib diisi.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(this, "Password tidak sama.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (InMemoryAuthStore.register(username, password)) {
                Toast.makeText(this, "Registrasi berhasil, silakan login.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Username sudah terdaftar.", Toast.LENGTH_SHORT).show()
            }
        }

        backToLogin.setOnClickListener {
            finish()
        }
    }
}


