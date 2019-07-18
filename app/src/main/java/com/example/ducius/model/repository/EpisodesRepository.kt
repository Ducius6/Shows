package com.example.ducius.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.MyShowsApp
import com.example.ducius.model.Episode
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

private const val FILENAME = "show_episodes"

object EpisodesRepository {

    private val episodesLiveData = MutableLiveData<Map<Int, List<Episode>>>()

    fun getEpisodes(): LiveData<Map<Int, List<Episode>>> =
        episodesLiveData

    private val episodesMap = readEpisodesStorage()

    init {
        episodesLiveData.value =
            episodesMap
    }

    fun readEpisodesStorage(): MutableMap<Int, MutableList<Episode>> {
        return try {
            ObjectInputStream(MyShowsApp.instance.openFileInput(FILENAME)).use {
                it.readObject() as MutableMap<Int, MutableList<Episode>>
            }
        } catch (ex: Exception) {
            mutableMapOf()
        }
    }

    fun addEpisode(episode: Episode, showId: Int) {
        if(episodesMap.get(showId) == null){
            val list = mutableListOf<Episode>()
            list.add(episode)
            episodesMap.put(showId, list)
        }else{
            val list = episodesMap.get(showId)
            list?.add(episode)
            list?.let { episodesMap.put(showId, it) }
        }

        episodesLiveData.value =
            episodesMap

        ObjectOutputStream(MyShowsApp.instance.openFileOutput(FILENAME, Context.MODE_PRIVATE)).use {
            it.writeObject(episodesMap)
        }
    }
}