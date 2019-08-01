package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.Episode
import com.example.ducius.model.PostEpisode
import com.example.ducius.model.repository.EpisodesRepository
import com.example.ducius.responses.CompleteEpisodeResponse
import com.example.ducius.responses.PostEpisodeResponse
import java.io.File

class AddEpisodeViewModel : ViewModel(), Observer<CompleteEpisodeResponse> {

    private val episodeLiveData = MutableLiveData<CompleteEpisodeResponse>()

    val liveData: LiveData<CompleteEpisodeResponse>
        get() {
            return episodeLiveData
        }

    fun postEpisodeData(imageFile: File?, episode: PostEpisode) {
        EpisodesRepository.postMedia(imageFile, episode)
    }

    init {
        EpisodesRepository.episodeResponse().observeForever(this)
    }

    override fun onCleared() {
        EpisodesRepository.episodeResponse().removeObserver(this)
    }

    override fun onChanged(episode: CompleteEpisodeResponse?) {
        episodeLiveData.value = episode
    }

    var seasonEpisode: String? = null
    var episodeImageURi: String? = null


    fun saveSeasonAndEpisode(seasonEpisode: String) {
        this.seasonEpisode = seasonEpisode
    }

    fun saveEpisodeImage(uri: String) {
        this.episodeImageURi = uri
    }

    fun getImageUri(): String? = episodeImageURi
}