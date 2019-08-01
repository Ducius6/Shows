package com.example.ducius.retrofit

import com.example.ducius.model.PostEpisode
import com.example.ducius.model.RegisterInfo
import com.example.ducius.responses.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("shows")
    fun getAllShows(): Call<ShowsResponse>

    @GET("shows/{id}")
    fun getShow(@Path("id") id: String): Call<ShowDetailsResponse>

    @GET("shows/{showId}/episodes")
    fun getShowEpisodes(@Path("showId") showId: String): Call<EpisodeResponse>

    @POST("users")
    fun registerUser(@Body registerInfo: RegisterInfo): Call<RegisterUserResponse>

    @POST("users/sessions")
    fun loginUser(@Body registerInfo: RegisterInfo): Call<LoginResponse>

    @POST("episodes")
    fun addEpisode(@Body episode: PostEpisode): Call<PostEpisodeResponse>

    @POST("media")
    @Multipart
    fun uploadMedia(@Part("file\"; filename=\"image.jpg\"") requestBody: RequestBody): Call<MediaResponse>

}