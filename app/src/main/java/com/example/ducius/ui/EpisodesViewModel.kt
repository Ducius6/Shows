package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.CompleteShow
import com.example.ducius.model.Show
import com.example.ducius.model.repository.ShowsRepository

class EpisodesViewModel : ViewModel(), Observer<CompleteShow> {

    private val showLiveData = MutableLiveData<CompleteShow>()

    val completeShowLiveData: LiveData<CompleteShow>
        get() {
            return showLiveData
        }

    fun getCompleteShow(showId: String) {
        ShowsRepository.fetchShowDetailsAndListOfEpisodes(showId)
    }

    init {
        ShowsRepository.completeShowLiveData().observeForever(this)
    }

    override fun onChanged(completeShow: CompleteShow?) {
        showLiveData.value = completeShow
    }

    override fun onCleared() {
        ShowsRepository.completeShowLiveData().removeObserver(this)
    }

    fun getFirstShow(): Show? = ShowsRepository.showsLiveData().value?.showsList?.first()
}