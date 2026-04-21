package com.example.uts_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.view.WindowInsetsCompat

class SeminarRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seminar_registration)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.formRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameInput = findViewById<EditText>(R.id.etName)
        val emailInput = findViewById<EditText>(R.id.etEmail)
        val phoneInput = findViewById<EditText>(R.id.etPhone)
        val genderGroup = findViewById<RadioGroup>(R.id.rgGender)
        val seminarSpinner = findViewById<Spinner>(R.id.spinnerSeminar)
        val genderError = findViewById<TextView>(R.id.tvGenderError)
        val seminarError = findViewById<TextView>(R.id.tvSeminarError)
        val agreementCheckbox = findViewById<CheckBox>(R.id.cbAgreement)
        val agreementError = findViewById<TextView>(R.id.tvAgreementError)
        val submitButton = findViewById<Button>(R.id.btnSubmitRegistration)

        val seminarOptions = resources.getStringArray(R.array.seminar_options)
        seminarSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            seminarOptions
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        nameInput.doAfterTextChanged {
            validateName(nameInput)
        }

        emailInput.doAfterTextChanged {
            validateEmail(emailInput)
        }

        phoneInput.doAfterTextChanged {
            val cleanValue = it.toString().filter { char -> char.isDigit() }
            if (cleanValue != it.toString()) {
                phoneInput.setText(cleanValue)
                phoneInput.setSelection(cleanValue.length)
            }
            validatePhone(phoneInput)
        }

        genderGroup.setOnCheckedChangeListener { _, _ ->
            validateGender(genderGroup, genderError)
        }

        seminarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                validateSeminar(seminarSpinner, seminarError)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                seminarError.visibility = View.VISIBLE
            }
        }

        agreementCheckbox.setOnCheckedChangeListener { _, _ ->
            validateAgreement(agreementCheckbox, agreementError)
        }

        submitButton.setOnClickListener {
            val isNameValid = validateName(nameInput)
            val isEmailValid = validateEmail(emailInput)
            val isPhoneValid = validatePhone(phoneInput)
            val isGenderValid = validateGender(genderGroup, genderError)
            val isSeminarValid = validateSeminar(seminarSpinner, seminarError)
            val isAgreementValid = validateAgreement(agreementCheckbox, agreementError)

            if (!isNameValid || !isEmailValid || !isPhoneValid || !isGenderValid || !isSeminarValid || !isAgreementValid) {
                if (!isAgreementValid) {
                    Toast.makeText(this, getString(R.string.form_error_agreement), Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val selectedGenderText = findViewById<RadioButton>(genderGroup.checkedRadioButtonId).text.toString()
            val seminar = seminarSpinner.selectedItem?.toString().orEmpty()

            showSubmitConfirmationDialog(name, email, phone, selectedGenderText, seminar)
        }
    }

    private fun showSubmitConfirmationDialog(
        name: String,
        email: String,
        phone: String,
        gender: String,
        seminar: String
    ) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setMessage(getString(R.string.form_submit_confirmation))
            .setPositiveButton(getString(R.string.form_confirm_yes)) { _, _ ->
                val intent = Intent(this, SeminarResultActivity::class.java).apply {
                    putExtra(SeminarResultActivity.EXTRA_NAME, name)
                    putExtra(SeminarResultActivity.EXTRA_EMAIL, email)
                    putExtra(SeminarResultActivity.EXTRA_PHONE, phone)
                    putExtra(SeminarResultActivity.EXTRA_GENDER, gender)
                    putExtra(SeminarResultActivity.EXTRA_SEMINAR, seminar)
                }
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.form_confirm_no), null)
            .show()
    }

    private fun validateName(nameInput: EditText): Boolean {
        val name = nameInput.text.toString().trim()
        return if (name.isBlank()) {
            nameInput.error = getString(R.string.form_error_name_required)
            false
        } else {
            nameInput.error = null
            true
        }
    }

    private fun validateEmail(emailInput: EditText): Boolean {
        val email = emailInput.text.toString().trim()
        return when {
            email.isBlank() -> {
                emailInput.error = getString(R.string.form_error_email_required)
                false
            }

            !email.contains("@") -> {
                emailInput.error = getString(R.string.form_error_email_invalid)
                false
            }

            else -> {
                emailInput.error = null
                true
            }
        }
    }

    private fun validatePhone(phoneInput: EditText): Boolean {
        val phone = phoneInput.text.toString().trim()
        val isDigitsOnly = phone.all { it.isDigit() }
        val isValidLength = phone.length in 10..13
        val startsCorrectly = phone.startsWith("08")

        return when {
            phone.isBlank() -> {
                phoneInput.error = getString(R.string.form_error_phone_required)
                false
            }

            !isDigitsOnly || !isValidLength || !startsCorrectly -> {
                phoneInput.error = getString(R.string.form_error_phone_invalid)
                false
            }

            else -> {
                phoneInput.error = null
                true
            }
        }
    }

    private fun validateGender(genderGroup: RadioGroup, errorView: TextView): Boolean {
        val isValid = genderGroup.checkedRadioButtonId != -1
        errorView.visibility = if (isValid) View.GONE else View.VISIBLE
        return isValid
    }

    private fun validateSeminar(seminarSpinner: Spinner, errorView: TextView): Boolean {
        val seminar = seminarSpinner.selectedItem?.toString().orEmpty()
        val isValid = seminar != getString(R.string.form_select_seminar_prompt)
        errorView.visibility = if (isValid) View.GONE else View.VISIBLE
        return isValid
    }

    private fun validateAgreement(agreementCheckbox: CheckBox, errorView: TextView): Boolean {
        val isValid = agreementCheckbox.isChecked
        errorView.visibility = if (isValid) View.GONE else View.VISIBLE
        return isValid
    }
}

