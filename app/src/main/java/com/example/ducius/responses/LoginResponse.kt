package com.example.ducius.responses

import com.example.ducius.model.Token
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LoginResponse(

    @Json(name = "data")
    val token: Token? = null,

    @Transient
    val isSucccessful: Boolean = true
)
