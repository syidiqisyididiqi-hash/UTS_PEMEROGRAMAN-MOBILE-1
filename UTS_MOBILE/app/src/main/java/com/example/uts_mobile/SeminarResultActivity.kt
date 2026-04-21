package com.example.uts_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SeminarResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seminar_result)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resultRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = intent.getStringExtra(EXTRA_NAME).orEmpty()
        val email = intent.getStringExtra(EXTRA_EMAIL).orEmpty()
        val phone = intent.getStringExtra(EXTRA_PHONE).orEmpty()
        val gender = intent.getStringExtra(EXTRA_GENDER).orEmpty()
        val seminar = intent.getStringExtra(EXTRA_SEMINAR).orEmpty()

        findViewById<TextView>(R.id.tvResultName).text = getString(R.string.result_name_format, name)
        findViewById<TextView>(R.id.tvResultEmail).text = getString(R.string.result_email_format, email)
        findViewById<TextView>(R.id.tvResultPhone).text = getString(R.string.result_phone_format, phone)
        findViewById<TextView>(R.id.tvResultGender).text = getString(R.string.result_gender_format, gender)
        findViewById<TextView>(R.id.tvResultSeminar).text = getString(R.string.result_seminar_format, seminar)

        findViewById<Button>(R.id.btnBackToMain).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_SEMINAR = "extra_seminar"
    }
}

