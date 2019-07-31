package com.example.ducius

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.ducius.fragment.ShowsContainerActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private val handler = Handler()

    companion object {
        const val USERNAME = "USERNAME"

        fun newInstance(context: Context, username: String): Intent =
            Intent(context, WelcomeActivity::class.java).putExtra(USERNAME, username)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeText.text = String.format("Welcome %s", intent.getStringExtra(USERNAME))

        handler.postDelayed({ doStuff() }, 3000)
    }

    private fun doStuff() {
        startActivity(Intent(this, ShowsContainerActivity::class.java))
    }
}
