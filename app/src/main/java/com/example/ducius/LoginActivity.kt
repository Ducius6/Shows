package com.example.ducius

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.text.Editable
import android.text.TextWatcher


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.isEnabled = false

        usernameEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
                loginButton.isEnabled = (usernameEditText.text.length>=1 && passwordEditText.text.length>=8)
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
                loginButton.isEnabled = (usernameEditText.text.length>=1 && passwordEditText.text.length>=8)
            }
        })

        loginButton.setOnClickListener {
            startActivity(WelcomeActivity.newInstance(this, usernameEditText.text.toString()))
        }

        //usernameInputLayout.error = "This username already exist!"
    }
}
