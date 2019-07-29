package com.example.ducius.retrofit

import android.content.Context
import com.example.ducius.MyShowsApp
import com.example.ducius.ui.LoginActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://api.infinum.academy/api/"
    private const val AUTHORIZATION = "Authorization"

    val token =
        MyShowsApp.instance.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(LoginActivity.TOKEN, "")

    var client = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "$token")
            .build()
        chain.proceed(newRequest)
    }.build()

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}