package com.example.ducius.ui

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.RegisterActivity
import com.example.ducius.fragment.ShowsContainerActivity
import com.example.ducius.model.RegisterInfo
import kotlinx.android.synthetic.main.activity_register.*

private const val MAX_PASSWORD_CHAR = 8

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    companion object{
        const val TOKEN = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        if (intent.getSerializableExtra("user") != null) {
            viewModel.getUserData(intent.getSerializableExtra("user") as RegisterInfo)
            viewModel.liveData.observe(this, Observer {
                if (it.isSucccessful) {
                    val token = it.token
                    val intent =
                        Intent(this, ShowsContainerActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(TOKEN, token?.token)
                    startActivity(intent)
                    finish()
                }
            })
        }

        loginButton.setOnClickListener {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(usernameEditText.text).matches()) {
                if (rememberMeCheckBox.isChecked) {
                    viewModel.savePreferences(usernameEditText.text.toString(), passwordEditText.text.toString())
                }
                viewModel.getUserData(RegisterInfo(usernameEditText.text.toString(), passwordEditText.text.toString()))
                viewModel.liveData.observe(this, Observer {
                    if (it.isSucccessful) {
                        val token = it.token
                        val intent =
                            Intent(this, ShowsContainerActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra(TOKEN, token?.token)
                        startActivity(intent)
                        finish()
                    }
                })
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

        createAccountTextView.setOnClickListener {
            startActivity(RegisterActivity.newInstance(this))
        }

        usernameEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editableText: Editable) {}

            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                loginButton.isEnabled =
                    (usernameEditText.text.isNotEmpty())
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loginButton.isEnabled =
                    (usernameEditText.text.isNotEmpty())
            }
        })
    }
}
