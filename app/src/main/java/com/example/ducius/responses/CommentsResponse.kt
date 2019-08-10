package com.example.ducius.responses

import com.example.ducius.model.Comment
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CommentsResponse(

    @Json(name = "data")
    val listOfComments: List<Comment>? = arrayListOf(),

    @Transient
    val isSuccessful: Boolean = true
)