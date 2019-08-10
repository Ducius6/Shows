package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.model.CompleteShow
import com.example.ducius.responses.EpisodeResponse
import com.example.ducius.responses.LikeResponse
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

    private val likesResponseLiveData = MutableLiveData<LikeResponse>()

    private var completeShowData = MutableLiveData<CompleteShow>()

    private var showDetailsResponse = ShowDetailsResponse()

    private var episodeResponse = EpisodeResponse()

    fun likesLiveData(): LiveData<LikeResponse> = likesResponseLiveData

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

    fun postLike(showId: String) {
        apiService?.postLike(showId)?.enqueue(object : Callback<LikeResponse> {
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                likesResponseLiveData.value = LikeResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                with(response){
                    if(isSuccessful && body()!=null){
                        likesResponseLiveData.value = LikeResponse(type = body()?.type, title = body()?.title, mediaId = body()?.mediaId, description =  body()?.description, id = body()?.id, likesCount = body()?.likesCount, isSuccessful = true)
                    }
                    else{
                        likesResponseLiveData.value = LikeResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun postDislike(showId: String) {
        apiService?.postDislike(showId)?.enqueue(object : Callback<LikeResponse> {
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                likesResponseLiveData.value = LikeResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                with(response){
                    if(isSuccessful && body()!=null){
                        likesResponseLiveData.value = LikeResponse(type = body()?.type, title = body()?.title, mediaId = body()?.mediaId, description =  body()?.description, id = body()?.id, likesCount = body()?.likesCount, isSuccessful = true)
                    }
                    else{
                        likesResponseLiveData.value = LikeResponse(isSuccessful = false)
                    }
                }
            }
        })
    }
}