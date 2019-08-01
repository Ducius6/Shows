package com.example.ducius.responses

import com.example.ducius.model.Media
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MediaResponse(

    @Json(name = "data")
    val media: Media? = null

)