package com.example.ducius.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ducius.model.PostComment
import com.example.ducius.model.PostEpisode
import com.example.ducius.responses.*
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

    private var episodeDetailsLiveData = MutableLiveData<EpisodeDetailsResponse>()

    private var commentsLiveData = MutableLiveData<CommentsResponse>()

    private var postCommentLiveData = MutableLiveData<PostCommentResponse>()

    private var postedEpisode = PostEpisodeResponse()

    private var postMedia = MediaResponse()

    private var mediaId: String = ""

    fun commentsLiveData(): LiveData<CommentsResponse> = commentsLiveData

    fun postedCommentLiveData(): LiveData<PostCommentResponse> = postCommentLiveData

    fun episodeDetailsLiveData(): LiveData<EpisodeDetailsResponse> = episodeDetailsLiveData

    fun episodeResponse(): LiveData<CompleteEpisodeResponse> = completeEpisodeResponse

    fun postEpisodeData(postEpisode: PostEpisode) {
        postEpisode.imageUrl = mediaId
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
                            isSuccessful = true
                        )
                    } else {
                        completeEpisodeResponse.value = CompleteEpisodeResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun postMedia(imageFile: File?, episodeData: PostEpisode) {
        apiService?.uploadMedia(RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
            ?.enqueue(object : Callback<MediaResponse> {
                override fun onFailure(call: Call<MediaResponse>, t: Throwable) {
                    t.localizedMessage
                    t.printStackTrace()
                    completeEpisodeResponse.value = CompleteEpisodeResponse(isSuccessful = false)
                }

                override fun onResponse(call: Call<MediaResponse>, response: Response<MediaResponse>) {
                    with(response) {
                        if (isSuccessful && body() != null) {
                            postMedia = MediaResponse(media = body()?.media)
                            mediaId = postMedia?.media?.id.toString()
                            postEpisodeData(postEpisode = episodeData)
                        } else {
                            completeEpisodeResponse.value = CompleteEpisodeResponse(isSuccessful = false)
                        }
                    }
                }
            })
    }

    fun getEpisodeDetails(episodeId: String) {
        apiService?.getEpisodeDetails(episodeId)?.enqueue(object : Callback<EpisodeDetailsResponse> {
            override fun onFailure(call: Call<EpisodeDetailsResponse>, t: Throwable) {
                t.localizedMessage
                t.printStackTrace()
                episodeDetailsLiveData.value = EpisodeDetailsResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<EpisodeDetailsResponse>, response: Response<EpisodeDetailsResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        episodeDetailsLiveData.value =
                            EpisodeDetailsResponse(episodeDetails = body()?.episodeDetails, isSuccessful = true)
                    } else {
                        episodeDetailsLiveData.value = EpisodeDetailsResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun getComments(episodeId: String) {
        apiService?.getEpisodeComments(episodeId)?.enqueue(object : Callback<CommentsResponse> {
            override fun onFailure(call: Call<CommentsResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                commentsLiveData.value = CommentsResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<CommentsResponse>, response: Response<CommentsResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        commentsLiveData.value = CommentsResponse(body()?.listOfComments, isSuccessful = true)
                    } else {
                        commentsLiveData.value = CommentsResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun postComment(postComment: PostComment) {
        apiService?.addComment(postComment)?.enqueue(object : Callback<PostCommentResponse> {
            override fun onFailure(call: Call<PostCommentResponse>, t: Throwable) {
                t.printStackTrace()
                t.localizedMessage
                postCommentLiveData.value = PostCommentResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<PostCommentResponse>, response: Response<PostCommentResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        postCommentLiveData.value =
                            PostCommentResponse(postedComment = body()?.postedComment, isSuccessful = true)
                    } else {
                        postCommentLiveData.value = PostCommentResponse(isSuccessful = false)
                    }
                }
            }
        })
    }
}