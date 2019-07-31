package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.RegisterInfo
import com.example.ducius.model.repository.UserRepository
import com.example.ducius.responses.RegisterUserResponse

class RegisterUserViewModel : ViewModel(), Observer<RegisterUserResponse> {

    private val userLiveData = MutableLiveData<RegisterUserResponse>()

    val liveData: LiveData<RegisterUserResponse>
        get() {
            return userLiveData
        }

    fun isEmailValid(email: String): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun arePasswordSame(firstPassword: String, secondPassword: String): Boolean = (firstPassword == secondPassword)

    fun getUserData(user: RegisterInfo) {
        UserRepository.fetchUserData(user)
    }

    init {
        UserRepository.usersLiveData().observeForever(this)
    }

    override fun onChanged(userResponse: RegisterUserResponse?) {
        userLiveData.value = userResponse
    }

    override fun onCleared() {
        UserRepository.usersLiveData().removeObserver(this)
    }
}