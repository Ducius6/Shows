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
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches() && passwordFirstTime.text.toString().equals(
                    passwordSecondTime.text.toString()) && passwordFirstTime.text.toString().isNotEmpty()
            ) {
                val user =
                    RegisterInfo(email = emailEditText.text.toString(), password = passwordFirstTime.text.toString())
                viewModel.getUserData(user)
                viewModel.liveData.observe(this, Observer {
                    if (it.isSuccessful) {
                        val intent =
                            Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, getString(R.string.wrong_input), Toast.LENGTH_LONG).show()
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
