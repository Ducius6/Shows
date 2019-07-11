package com.example.ducius

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity

const val MAX_PASSWORD_CHAR = 8

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.isEnabled = false

        usernameEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editableText: Editable) {}

            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                loginButton.isEnabled =
                    (usernameEditText.text.isNotEmpty() && passwordEditText.text.length >= MAX_PASSWORD_CHAR)
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loginButton.isEnabled =
                    (usernameEditText.text.isNotEmpty() && passwordEditText.text.length >= MAX_PASSWORD_CHAR)
            }
        })

        loginButton.setOnClickListener {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(usernameEditText.text).matches()) {
                startActivity(WelcomeActivity.newInstance(this, usernameEditText.text.toString()))
            } else {
                usernameInputLayout.error = "This username already exist!"
            }
        }
    }
}