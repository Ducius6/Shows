package com.example.ducius.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R

private const val MAX_PASSWORD_CHAR = 8
private const val SAVED_USERNAME = "saved_username"
private const val SAVED_PASSWORD = "saved_password"

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.isEnabled = false
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

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
                startActivity(ShowsActivity.newInstance(this))
            } else {
                usernameInputLayout.error = getString(R.string.invalid_password)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        usernameEditText.setText(viewModel.loadUsernameFromPrefrences())
        passwordEditText.setText(viewModel.loadPasswordFromPrefrences())
        if (usernameEditText.text.toString().trim().isNotEmpty() && passwordEditText.text.toString().trim().isNotEmpty()) loginButton.performClick()
    }

    override fun onPause() {
        super.onPause()
        if (rememberMeCheckBox.isChecked)
            viewModel.savePreferences(usernameEditText.text.toString(), passwordEditText.text.toString())
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(SAVED_USERNAME, usernameInputLayout.editText?.text.toString())
        savedInstanceState.putString(SAVED_PASSWORD, passwordInputLayout.editText?.text.toString())
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        usernameEditText.setText(savedInstanceState.getString(SAVED_USERNAME))
        passwordInputLayout.editText?.setText(savedInstanceState.getString(SAVED_PASSWORD))
    }
}
