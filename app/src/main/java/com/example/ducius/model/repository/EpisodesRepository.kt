package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.model.PostEpisode
import com.example.ducius.responses.PostEpisodeResponse
import com.example.ducius.retrofit.Api
import com.example.ducius.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object EpisodesRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private var postedEpisode = MutableLiveData<PostEpisodeResponse>()

    fun episodePosted(): LiveData<PostEpisodeResponse> = postedEpisode

    fun postEpisodeData(postEpisode: PostEpisode) {
        apiService?.addEpisode(postEpisode)?.enqueue(object : Callback<PostEpisodeResponse> {
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
}