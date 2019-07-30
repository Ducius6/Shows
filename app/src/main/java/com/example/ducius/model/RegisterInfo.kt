package com.example.ducius.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class RegisterInfo(

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String

):Serializable
