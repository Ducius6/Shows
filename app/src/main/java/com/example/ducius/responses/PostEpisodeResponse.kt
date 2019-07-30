package com.example.ducius.responses

import com.example.ducius.model.EpisodeReturned
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostEpisodeResponse(

    @Json(name = "data")
    val returnedEpisode: EpisodeReturned? = null,

    @Transient
    val isSuccessful: Boolean = true
)