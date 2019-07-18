package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.Episode
import com.example.ducius.model.repository.EpisodesRepository

class EpisodesViewModel : ViewModel(), Observer<Map<Int, List<Episode>>> {

    private val episodesLiveData = MutableLiveData<Map<Int, List<Episode>>>()

    val liveData: LiveData<Map<Int, List<Episode>>>
        get() {
            return episodesLiveData
        }

    private var episodeMap = mapOf<Int, List<Episode>>()

    init {
        episodesLiveData.value = episodeMap
        EpisodesRepository.getEpisodes().observeForever(this)
    }

    override fun onChanged(episodes: Map<Int, List<Episode>>?) {
        episodeMap = episodes ?: mapOf()
        episodesLiveData.value = episodeMap
    }

    fun addEpisode(episode: Episode, showId: Int) {
        EpisodesRepository.addEpisode(episode, showId)
    }

    override fun onCleared() {
        EpisodesRepository.getEpisodes().removeObserver(this)
    }
}