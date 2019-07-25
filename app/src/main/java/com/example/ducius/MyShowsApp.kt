package com.example.ducius

import android.app.Application

class MyShowsApp : Application() {

    companion object {
        lateinit var instance: MyShowsApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}