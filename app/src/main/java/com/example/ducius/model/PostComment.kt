package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostComment(

    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String

)