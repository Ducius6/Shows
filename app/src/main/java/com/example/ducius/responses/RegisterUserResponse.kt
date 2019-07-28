package com.example.ducius.responses

import com.example.ducius.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterUserResponse(

    @Json(name = "data")
    val user: User? = null,

    @Transient
    val isSuccessful: Boolean = true
)