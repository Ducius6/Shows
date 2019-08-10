package com.example.ducius.responses

import com.example.ducius.model.PostedComment
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PostCommentResponse(

    @Json(name = "data")
    val postedComment: PostedComment? = null,

    @Transient
    val isSuccessful: Boolean = true

)