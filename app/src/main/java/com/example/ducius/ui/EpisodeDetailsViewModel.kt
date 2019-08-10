package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.repository.EpisodesRepository
import com.example.ducius.responses.EpisodeDetailsResponse

class EpisodeDetailsViewModel : ViewModel(), Observer<EpisodeDetailsResponse> {

    private val episodeDetailsLiveData = MutableLiveData<EpisodeDetailsResponse>()

    val liveData: LiveData<EpisodeDetailsResponse>
        get() {
            return episodeDetailsLiveData
        }

    fun getEpisodeDetailsData(episodeId: String) {
        EpisodesRepository.getEpisodeDetails(episodeId)
    }

    init {
        EpisodesRepository.episodeDetailsLiveData().observeForever(this)
    }

    override fun onCleared() {
        EpisodesRepository.episodeDetailsLiveData().removeObserver(this)
    }


    override fun onChanged(episodeDetailsResponse: EpisodeDetailsResponse?) {
        episodeDetailsLiveData.value = episodeDetailsResponse
    }
}