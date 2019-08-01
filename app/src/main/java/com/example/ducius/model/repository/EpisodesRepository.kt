package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.model.Media
import com.example.ducius.model.PostEpisode
import com.example.ducius.responses.CompleteEpisodeResponse
import com.example.ducius.responses.MediaResponse
import com.example.ducius.responses.PostEpisodeResponse
import com.example.ducius.retrofit.Api
import com.example.ducius.retrofit.RetrofitClient
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


object EpisodesRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private var completeEpisodeResponse = MutableLiveData<CompleteEpisodeResponse>()

    private var postedEpisode = PostEpisodeResponse()

    private var postMedia = MediaResponse()

    fun episodeResponse(): LiveData<CompleteEpisodeResponse> = completeEpisodeResponse

    fun postEpisodeData(postEpisode: PostEpisode) {
        apiService?.addEpisode(postEpisode)?.enqueue(object : Callback<PostEpisodeResponse> {
            override fun onFailure(call: Call<PostEpisodeResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                completeEpisodeResponse.value = CompleteEpisodeResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<PostEpisodeResponse>, response: Response<PostEpisodeResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        postedEpisode = PostEpisodeResponse(body()?.returnedEpisode)
                        completeEpisodeResponse.value = CompleteEpisodeResponse(
                            mediaResponse = postMedia,
                            postEpisodeResponse = postedEpisode,
                            isSuccessful = true)
                    } else {
                        completeEpisodeResponse.value = CompleteEpisodeResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun postMedia(imageFile: File?, episodeData: PostEpisode) {
        apiService?.uploadMedia(RequestBody.create(MediaType.parse("image/webp"), imageFile))
            ?.enqueue(object : Callback<MediaResponse> {
                override fun onFailure(call: Call<MediaResponse>, t: Throwable) {
                    t.localizedMessage
                    t.printStackTrace()
                    completeEpisodeResponse.value = CompleteEpisodeResponse(isSuccessful = false)
                }

                override fun onResponse(call: Call<MediaResponse>, response: Response<MediaResponse>) {
                    with(response) {
                        if (isSuccessful && body() != null) {
                            postMedia = MediaResponse(body()?.media)
                            episodeData.imageUrl = postMedia?.media?.path.toString()
                            postEpisodeData(postEpisode = episodeData)
                        } else {
                            completeEpisodeResponse.value = CompleteEpisodeResponse(isSuccessful = false)
                        }
                    }
                }
            })
    }
}