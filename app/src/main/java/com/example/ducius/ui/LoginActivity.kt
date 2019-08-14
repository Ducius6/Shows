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
import com.example.ducius.WelcomeActivity
import com.example.ducius.fragment.ShowsContainerActivity
import com.example.ducius.model.RegisterInfo

private const val EMPTY_STRING = ""

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private var user: RegisterInfo? = null
    private var isCalledRegister = false
    private var isCalledLogin = false

    companion object {
        const val TOKEN = "token"
        const val PREFS_NAME = "preferences"
        var token: String = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        user = intent.getSerializableExtra(getString(R.string.user_var)) as RegisterInfo?
        if (user != null) {
            viewModel.getUserData(intent.getSerializableExtra(getString(R.string.user_var)) as RegisterInfo)
            viewModel.liveData.observe(this, Observer {
                if (it.isSucccessful) {
                    if (isCalledRegister.not()) {
                        isCalledRegister = true
                        token = it.token?.token.toString()
                        it.token?.token?.let { it1 -> viewModel.saveToken(it1) }
                        startActivity(user?.email?.let { it1 -> WelcomeActivity.newInstance(this, it1) })
                        finish()
                    }
                }
            })
        }

        loginButton.setOnClickListener {
            usernameInputLayout.error = EMPTY_STRING
            passwordInputLayout.error = EMPTY_STRING
            if (viewModel.isEmailValid(usernameEditText.text.toString()).not()) {
                usernameInputLayout.error = getString(R.string.wrong_email_input)
            } else if (passwordEditText.text.toString().isEmpty()) {
                passwordInputLayout.error = getString(R.string.invalid_password)
            } else {
                loginButton.isEnabled = false
                if (rememberMeCheckBox.isChecked) {
                    viewModel.savePreferences(usernameEditText.text.toString(), passwordEditText.text.toString())
                }
                viewModel.getUserData(RegisterInfo(usernameEditText.text.toString(), passwordEditText.text.toString()))
                viewModel.liveData.observe(this, Observer {
                    if (it.isSucccessful) {
                        if (isCalledLogin.not()) {
                            isCalledLogin = true
                            token = it.token?.token.toString()
                            it.token?.token?.let { it1 -> viewModel.saveToken(it1) }
                            startActivity(
                                Intent(
                                    this,
                                    ShowsContainerActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                        }
                    }
                })
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
