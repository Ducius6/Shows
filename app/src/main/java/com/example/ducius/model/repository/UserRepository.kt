package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.model.RegisterInfo
import com.example.ducius.responses.LoginResponse
import com.example.ducius.responses.RegisterUserResponse
import com.example.ducius.retrofit.Api
import com.example.ducius.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private val userLiveData = MutableLiveData<RegisterUserResponse>()

    private val loginLiveData = MutableLiveData<LoginResponse>()

    fun loginData(): LiveData<LoginResponse> = loginLiveData

    fun usersLiveData(): LiveData<RegisterUserResponse> = userLiveData

    fun fetchLoginUserData(user: RegisterInfo) {
        apiService?.loginUser(user)?.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                loginLiveData.value = LoginResponse(isSucccessful = false)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        loginLiveData.value = LoginResponse(token = body()?.token, isSucccessful = true)
                    } else {
                        loginLiveData.value = LoginResponse(isSucccessful = false)
                    }
                }
            }
        })
    }

    fun fetchUserData(user: RegisterInfo) {
        apiService?.registerUser(user)?.enqueue(object : Callback<RegisterUserResponse> {
            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                t.localizedMessage
                t.printStackTrace()
                userLiveData.value = RegisterUserResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<RegisterUserResponse>, response: Response<RegisterUserResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        userLiveData.value = RegisterUserResponse(user = body()?.user, isSuccessful = true)
                    } else {
                        userLiveData.value = RegisterUserResponse(isSuccessful = false)
                    }
                }
            }
        })
    }
}