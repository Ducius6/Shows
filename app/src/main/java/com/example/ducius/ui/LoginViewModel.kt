package com.example.ducius.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ducius.MyShowsApp

class LoginViewModel : ViewModel() {

    private val defaultUsernameValue = ""
    private val defaultPasswordValue = ""
    private val PREFS_NAME = "preferences"
    private val PREF_USERNAME = "Username"
    private val PREF_PASSWORD = "Password"

    fun savePreferences(username: String, password: String) {
        val settings = MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(settings.edit()) {
            putString(PREF_USERNAME, username)
            putString(PREF_PASSWORD, password)
            apply()
        }
    }

    fun loadUsernameFromPrefrences(): String? =
        MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(
            PREF_USERNAME,
            defaultUsernameValue
        )

    fun loadPasswordFromPrefrences(): String? =
        MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(
            PREF_PASSWORD,
            defaultPasswordValue
        )
}