package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.Episode
import com.example.ducius.model.PostEpisode
import com.example.ducius.model.repository.EpisodesRepository
import com.example.ducius.responses.PostEpisodeResponse

class AddEpisodeViewModel : ViewModel(), Observer<PostEpisodeResponse> {

    private val episodeLiveData = MutableLiveData<PostEpisodeResponse>()

    val liveData: LiveData<PostEpisodeResponse>
        get() {
            return episodeLiveData
        }

    fun postEpisodeData(episode:PostEpisode, authHeader:String) {
        EpisodesRepository.postEpisodeData(episode, authHeader)
    }

    init {
        EpisodesRepository.episodePosted().observeForever(this)
    }

    override fun onCleared() {
        EpisodesRepository.episodePosted().removeObserver(this)
    }

    override fun onChanged(postEpisode: PostEpisodeResponse?) {
        episodeLiveData.value = postEpisode
    }

    var seasonEpisode: String? = null
    var episodeImageURi: String? = null


    fun saveSeasonAndEpisode(seasonEpisode: String) {
        this.seasonEpisode = seasonEpisode
    }

    fun saveEpisodeImage(uri: String) {
        this.episodeImageURi = uri
    }
}