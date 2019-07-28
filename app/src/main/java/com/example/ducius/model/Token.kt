package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class Token(

    @Json(name = "token")
    val token: String

) : Serializable