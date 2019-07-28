package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.Show
import com.example.ducius.model.repository.EpisodesRepository
import com.example.ducius.model.repository.ShowsRepository
import com.example.ducius.responses.EpisodeResponse
import com.example.ducius.responses.ShowDetailsResponse

class EpisodesViewModel : ViewModel(), Observer<Any> {

    private val episodesLiveData = MutableLiveData<EpisodeResponse>()

    private val showDetailsLiveData = MutableLiveData<ShowDetailsResponse>()

    val episodeLiveData: LiveData<EpisodeResponse>
        get() {
            return episodesLiveData
        }

    val detailsLiveData: LiveData<ShowDetailsResponse>
        get() {
            return showDetailsLiveData
        }

    fun getShowDetailsLiveData(showId: String) {
        ShowsRepository.fetchShowDetails(showId)
    }

    fun getEpisodeData(showId: String) {
        EpisodesRepository.fetchEpisodesData(showId)
    }

    init {
        ShowsRepository.detailsLiveData().observeForever(this)
        EpisodesRepository.episodesLiveData().observeForever(this)
    }

    override fun onChanged(any: Any?) {
        if (any is EpisodeResponse) {
            episodesLiveData.value = any
        } else if (any is ShowDetailsResponse) {
            showDetailsLiveData.value = any
        }
    }

    override fun onCleared() {
        ShowsRepository.detailsLiveData().removeObserver(this)
        EpisodesRepository.episodesLiveData().removeObserver(this)
    }

    fun getFirstShow(): Show? = ShowsRepository.showsLiveData().value?.showsList?.first()
}