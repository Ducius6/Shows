package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.responses.ShowsResponse
import com.example.ducius.model.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<ShowsResponse> {

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
}