package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class EpisodeReturned(

    @Json(name = "showId")
    val showId: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "imageUrl")
    val imageUrl: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "episodeNumber")
    val episodeNumber: String,

    @Json(name = "season")
    val season: String,

    @Json(name = "_id")
    val id: String

) : Serializable