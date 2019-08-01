package com.example.ducius.retrofit

import android.content.Context
import com.example.ducius.MyShowsApp
import com.example.ducius.ui.LoginActivity
import com.readystatesoftware.chuck.ChuckInterceptor
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
            .header(AUTHORIZATION, "$token")
            .build()
        chain.proceed(newRequest)
    }.build()

    var clientChuck = OkHttpClient.Builder()
        .addInterceptor(ChuckInterceptor(MyShowsApp.instance))
        .build()

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .client(clientChuck)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}