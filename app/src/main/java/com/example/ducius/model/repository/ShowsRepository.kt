package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var showDetailsLiveData = MutableLiveData<ShowDetailsResponse>()

    fun detailsLiveData():LiveData<ShowDetailsResponse> = showDetailsLiveData

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

    fun fetchShowDetails(showId: String) {
        apiService?.getShow(showId)?.enqueue(object : Callback<ShowDetailsResponse> {
            override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                showDetailsLiveData.value = ShowDetailsResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        showDetailsLiveData.value = ShowDetailsResponse(show = body()?.show, isSuccessful = true)
                    } else {
                        showDetailsLiveData.value = ShowDetailsResponse(isSuccessful = false)
                    }
                }
            }
        })
    }
}