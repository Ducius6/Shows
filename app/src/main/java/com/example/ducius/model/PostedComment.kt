package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostedComment(

    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "userEmail")
    val userEmail: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "_id")
    val id: String

)