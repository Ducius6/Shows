package com.example.ducius.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.MyShowsApp
import com.example.ducius.model.RegisterInfo
import com.example.ducius.model.repository.UserRepository
import com.example.ducius.responses.LoginResponse
import com.example.ducius.responses.RegisterUserResponse

class LoginViewModel : ViewModel(), Observer<LoginResponse> {

    private val loginLiveData = MutableLiveData<LoginResponse>()
    private val defaultUsernameValue = ""
    private val defaultPasswordValue = ""
    private val PREFS_NAME = "preferences"
    private val PREF_USERNAME = "Username"
    private val PREF_PASSWORD = "Password"

    val liveData: LiveData<LoginResponse>
        get() {
            return loginLiveData
        }

    fun getUserData(user: RegisterInfo) {
        UserRepository.fetchLoginUserData(user)
    }

    init {
        UserRepository.loginData().observeForever(this)
    }


    override fun onCleared() {
        UserRepository.loginData().removeObserver(this)
    }

    override fun onChanged(loginResponse: LoginResponse?) {
        loginLiveData.value = loginResponse
    }

    fun savePreferences(username: String, password: String) {
        val settings = MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(settings.edit()) {
            putString(PREF_USERNAME, username)
            putString(PREF_PASSWORD, password)
            apply()
        }
    }

    fun saveToken(token: String) {
        with(MyShowsApp.instance.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE).edit()) {
            putString(LoginActivity.TOKEN, token)
            apply()
        }
    }

    fun isEmailValid(email: String): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

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