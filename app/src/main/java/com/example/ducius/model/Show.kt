package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class Show(

    @Json(name = "_id")
    val ID: String,

    @Json(name = "title")
    val name: String,

    @Json(name = "imageUrl")
    val imageURL: String,

    @Json(name = "likesCount")
    val likesCount: Int

) : Serializable