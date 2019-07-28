package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class User(

    @Json(name = "type")
    val type: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "_id")
    val id: String
) : Serializable