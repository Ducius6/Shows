package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class ShowDetails(
    @Json(name = "type")
    val type: String,

    @Json(name = "title")
    val name: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "_id")
    val ID: String,

    @Json(name = "likesCount")
    val likesCount: Int,

    @Json(name = "imageUrl")
    val imageURL: String

) : Serializable