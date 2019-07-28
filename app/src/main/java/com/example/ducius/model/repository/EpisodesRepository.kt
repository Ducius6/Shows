package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.model.PostEpisode
import com.example.ducius.responses.EpisodeResponse
import com.example.ducius.responses.PostEpisodeResponse
import com.example.ducius.retrofit.Api
import com.example.ducius.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object EpisodesRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private val episodesLiveData = MutableLiveData<EpisodeResponse>()

    private var postedEpisode = MutableLiveData<PostEpisodeResponse>()

    fun episodePosted(): LiveData<PostEpisodeResponse> = postedEpisode
    fun episodesLiveData(): LiveData<EpisodeResponse> = episodesLiveData

    fun postEpisodeData(postEpisode: PostEpisode, authHeader:String) {
        apiService?.addEpisode(postEpisode, authHeader)?.enqueue(object : Callback<PostEpisodeResponse> {
            override fun onFailure(call: Call<PostEpisodeResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                postedEpisode.value = PostEpisodeResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<PostEpisodeResponse>, response: Response<PostEpisodeResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        postedEpisode.value =
                            PostEpisodeResponse(returnedEpisode = body()?.returnedEpisode, isSuccessful = true)
                    } else {
                        postedEpisode.value = PostEpisodeResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun fetchEpisodesData(showId: String) {
        apiService?.getShowEpisodes(showId)?.enqueue(object : Callback<EpisodeResponse> {
            override fun onFailure(call: Call<EpisodeResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                episodesLiveData.value = EpisodeResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<EpisodeResponse>, response: Response<EpisodeResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        episodesLiveData.value =
                            EpisodeResponse(listOfEpisodes = body()?.listOfEpisodes, isSuccessful = true)
                    } else {
                        episodesLiveData.value = EpisodeResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

}