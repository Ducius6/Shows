package com.example.ducius.responses

import com.example.ducius.model.Episode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeResponse(

    @Json(name = "data")
    val listOfEpisodes: List<Episode>? = arrayListOf(),

    @Transient
    val isSuccessful: Boolean = true
)