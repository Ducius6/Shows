package com.example.ducius.ui

import androidx.lifecycle.ViewModel
import com.example.ducius.model.Episode
import com.example.ducius.model.repository.EpisodesRepository

class AddEpisodeViewModel : ViewModel() {

    var seasonEpisode: String? = null
    var episodeImageURi: String? = null

    fun addEpisode(episode: Episode, showId: Int) {
        EpisodesRepository.addEpisode(episode, showId)
    }

    fun saveSeasonAndEpisode(seasonEpisode:String){
        this.seasonEpisode = seasonEpisode
    }

    fun saveEpisodeImage(uri: String){
        this.episodeImageURi = uri
    }
}