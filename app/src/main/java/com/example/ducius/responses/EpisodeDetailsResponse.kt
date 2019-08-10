package com.example.ducius.responses

import com.example.ducius.model.EpisodeDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EpisodeDetailsResponse(

    @Json(name = "data")
    val episodeDetails: EpisodeDetails? = null,

    @Transient
    val isSuccessful: Boolean = true

)