package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(

    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String,

    @Json(name = "userEmail")
    val userEmail: String,

    @Json(name = "_id")
    val id: String

)