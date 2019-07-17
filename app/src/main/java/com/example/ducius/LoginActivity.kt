package com.example.ducius

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.content.Context

private const val MAX_PASSWORD_CHAR = 8
private const val PREFS_NAME = "preferences"
private const val PREF_USERNAME = "Username"
private const val PREF_PASSWORD = "Password"
private const val SAVED_USERNAME = "saved_username"
private const val SAVED_PASSWORD = "saved_password"

private const val defaultUsernameValue = ""
private const val defaultPasswordValue = ""

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
                startActivity(ShowsActivity.newInstance(this))
            } else {
                usernameInputLayout.error = getString(R.string.invalid_password)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this.loadPreferences()
    }

    override fun onPause() {
        super.onPause()
        this.savePreferences()
    }

    private fun savePreferences() {
        if (rememberMeCheckBox.isChecked) {
            val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            with(settings.edit()) {
                putString(PREF_USERNAME, usernameEditText.text.toString())
                putString(PREF_PASSWORD, passwordEditText.text.toString())
                apply()
            }
        }
    }

    private fun loadPreferences() {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        usernameEditText.setText(settings.getString(PREF_USERNAME, defaultUsernameValue))
        passwordEditText.setText(settings.getString(PREF_PASSWORD, defaultPasswordValue))
        if (usernameEditText.text.toString().trim().isNotEmpty() && passwordEditText.text.toString().trim().isNotEmpty()) loginButton.performClick()
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
