package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.Show
import com.example.ducius.model.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<List<Show>> {

    private val showsLiveData = MutableLiveData<List<Show>>()

    val liveData: LiveData<List<Show>>
        get() {
            return showsLiveData
        }

    private var showsList = listOf<Show>()

    init {
        showsLiveData.value = showsList
        ShowsRepository.getShows().observeForever(this)
    }

    override fun onChanged(shows: List<Show>?) {
        showsList = shows ?: listOf()
        showsLiveData.value = showsList
    }

    override fun onCleared() {
        ShowsRepository.getShows().removeObserver(this)
    }
}