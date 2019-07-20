package com.example.ducius.ui

import androidx.lifecycle.ViewModel
import com.example.ducius.model.Episode
import com.example.ducius.model.repository.EpisodesRepository

class AddEpisodeViewModel : ViewModel() {

    fun addEpisode(episode: Episode, showId: Int) {
        EpisodesRepository.addEpisode(episode, showId)
    }
}