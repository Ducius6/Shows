package com.example.ducius.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LikeResponse(

    @Json(name = "type")
    val type: String? = null,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "mediaId")
    val mediaId: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "_id")
    val id: String? = null,

    @Json(name = "likesCount")
    val likesCount: Int? = null,

    @Transient
    val isSuccessful: Boolean = true

)