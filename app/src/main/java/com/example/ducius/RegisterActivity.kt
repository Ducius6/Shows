package com.example.ducius

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.model.RegisterInfo
import com.example.ducius.ui.LoginActivity
import com.example.ducius.ui.RegisterUserViewModel
import kotlinx.android.synthetic.main.activity_register.*

private const val EMPTY_STRING = ""
class RegisterActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent =
            Intent(context, RegisterActivity::class.java)
    }

    private lateinit var viewModel: RegisterUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(registerToolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        viewModel = ViewModelProviders.of(this).get(RegisterUserViewModel::class.java)

        registerUserButton.setOnClickListener {
            emailInputLayout.error = EMPTY_STRING
            firstTimeTextInputLayout.error = EMPTY_STRING
            secondTimeTextInputLayout.error = EMPTY_STRING
            if(emailEditText.text.toString().isEmpty() || passwordFirstTime.text.toString().isEmpty() || passwordSecondTime.text.toString().isEmpty()){
                Toast.makeText(this, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_LONG).show()
            }
            else if (viewModel.isEmailValid(emailEditText.text.toString()).not()) {
                emailInputLayout.error = getString(R.string.wrong_email_input)
            }else if(viewModel.isPassordLongEnough(passwordFirstTime.text.toString()).not()){
                firstTimeTextInputLayout.error = getString(R.string.password_not_long_enough)
            } else if(viewModel.arePasswordSame(passwordFirstTime.text.toString(), passwordSecondTime.text.toString()).not()){
                secondTimeTextInputLayout.error = getString(R.string.password_dont_match)
            }
            else {
                val user =
                    RegisterInfo(email = emailEditText.text.toString(), password = passwordFirstTime.text.toString())
                viewModel.getUserData(user)
                viewModel.liveData.observe(this, Observer {
                    if (it.isSuccessful) {
                        startActivity(
                            Intent(
                                this,
                                LoginActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(getString(R.string.user_var), user)
                        )
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
