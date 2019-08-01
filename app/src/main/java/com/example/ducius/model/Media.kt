package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Media(

    @Json(name = "path")
    val path: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "_id")
    val id: String
)