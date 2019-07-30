package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.model.CompleteShow
import com.example.ducius.responses.EpisodeResponse
import com.example.ducius.responses.ShowDetailsResponse
import com.example.ducius.retrofit.Api
import com.example.ducius.retrofit.RetrofitClient
import com.example.ducius.responses.ShowsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ShowsRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private val showsResponseLiveData = MutableLiveData<ShowsResponse>()

    private var completeShowData = MutableLiveData<CompleteShow>()

    private var showDetailsResponse = ShowDetailsResponse()

    private var episodeResponse = EpisodeResponse()

    fun completeShowLiveData(): LiveData<CompleteShow> = completeShowData

    fun showsLiveData(): LiveData<ShowsResponse> = showsResponseLiveData

    fun fetchShowsData() {
        apiService?.getAllShows()?.enqueue(object : Callback<ShowsResponse> {
            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                t.printStackTrace()
                showsResponseLiveData.value = ShowsResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        showsResponseLiveData.value = ShowsResponse(
                            showsList = body()?.showsList,
                            isSuccessful = true
                        )
                    } else {
                        showsResponseLiveData.value = ShowsResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun fetchShowDetailsAndListOfEpisodes(showId: String) {
        apiService?.getShow(showId)?.enqueue(object : Callback<ShowDetailsResponse> {
            override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                completeShowData.value = CompleteShow(isSuccessful = false)
            }

            override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        fetchEpisodesData(showId)
                        showDetailsResponse = ShowDetailsResponse(show = body()?.show)
                    } else {
                        completeShowData.value = CompleteShow(isSuccessful = false)
                    }
                }
            }
        })
    }

    private fun fetchEpisodesData(showId: String) {
        apiService?.getShowEpisodes(showId)?.enqueue(object : Callback<EpisodeResponse> {
            override fun onFailure(call: Call<EpisodeResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                completeShowData.value = CompleteShow(isSuccessful = false)
            }

            override fun onResponse(call: Call<EpisodeResponse>, response: Response<EpisodeResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        episodeResponse = EpisodeResponse(listOfEpisodes = body()?.listOfEpisodes)
                        completeShowData.value = CompleteShow(episodeResponse, showDetailsResponse, isSuccessful = true)
                    } else {
                        completeShowData.value = CompleteShow(isSuccessful = false)
                    }
                }
            }
        })
    }
}