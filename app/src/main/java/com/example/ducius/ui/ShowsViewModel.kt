package com.example.ducius.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.MyShowsApp
import com.example.ducius.responses.ShowsResponse
import com.example.ducius.model.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<ShowsResponse> {

    private val PREFS_NAME = "preferences"
    private val PREF_USERNAME = "Username"
    private val PREF_PASSWORD = "Password"
    private val showsLiveData = MutableLiveData<ShowsResponse>()

    val liveData: LiveData<ShowsResponse>
        get() {
            return showsLiveData
        }

    fun getShowData(){
        ShowsRepository.fetchShowsData()
    }

    init {
        ShowsRepository.showsLiveData().observeForever(this)
    }

    override fun onChanged(showsResponseData: ShowsResponse?) {
        showsLiveData.value = showsResponseData
    }

    override fun onCleared() {
        ShowsRepository.showsLiveData().removeObserver(this)
    }

    fun clearUsernameAndPassword(){
        MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).apply {
            edit().remove(PREF_USERNAME).remove(PREF_PASSWORD).apply()
        }
    }
}