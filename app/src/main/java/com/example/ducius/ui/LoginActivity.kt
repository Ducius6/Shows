package com.example.ducius.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R

private const val MAX_PASSWORD_CHAR = 8

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        loginButton.setOnClickListener {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(usernameEditText.text).matches()) {
                if (rememberMeCheckBox.isChecked) {
                    viewModel.savePreferences(usernameEditText.text.toString(), passwordEditText.text.toString())
                }
                startActivity(ShowsActivity.newInstance(this))
            } else {
                usernameInputLayout.error = getString(R.string.invalid_password)
            }
        }

        usernameEditText.setText(viewModel.loadUsernameFromPrefrences())
        passwordEditText.setText(viewModel.loadPasswordFromPrefrences())
        if (usernameEditText.text.toString().trim().isNotEmpty() && passwordEditText.text.toString().trim().isNotEmpty()) {
            loginButton.isEnabled = true
            loginButton.performClick()
        }

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
    }
}
