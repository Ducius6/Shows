package com.example.ducius.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.MyShowsApp
import com.example.ducius.model.CompleteShow
import com.example.ducius.model.Show
import com.example.ducius.model.repository.ShowsRepository
import com.example.ducius.responses.LikeResponse

class EpisodesViewModel : ViewModel(), Observer<Any> {

    private val showLiveData = MutableLiveData<CompleteShow>()
    private val likesLiveData = MutableLiveData<LikeResponse>()
    private val PREFS_NAME = "preferences"
    private val LIKES_COUNT = "likesCount"

    val completeShowLiveData: LiveData<CompleteShow>
        get() {
            return showLiveData
        }

    val likeLiveData: LiveData<LikeResponse>
        get() {
            return likesLiveData
        }

    fun getCompleteShow(showId: String) {
        ShowsRepository.fetchShowDetailsAndListOfEpisodes(showId)
    }

    fun likeShow(showId: String) {
        ShowsRepository.postLike(showId)
    }

    fun dislikeShow(showId: String) {
        ShowsRepository.postDislike(showId)
    }

    init {
        ShowsRepository.likesLiveData().observeForever(this)
        ShowsRepository.completeShowLiveData().observeForever(this)
    }

    override fun onChanged(any: Any?) {
        if (any is CompleteShow) {
            showLiveData.value = any
        } else if (any is LikeResponse) {
            likesLiveData.value = any
        }
    }

    override fun onCleared() {
        ShowsRepository.likesLiveData().removeObserver(this)
        ShowsRepository.completeShowLiveData().removeObserver(this)
    }

    fun saveLike(showId: String) {
        val settings = MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(settings.edit()) {
            putInt(showId, 1)
            apply()
        }
    }

    fun saveDislike(showId: String) {
        val settings = MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(settings.edit()) {
            putInt(showId, -1)
            apply()
        }
    }

    fun getLikeorDislike(showId: String): Int {
        val settings = MyShowsApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return settings.getInt(showId, 0)
    }
}