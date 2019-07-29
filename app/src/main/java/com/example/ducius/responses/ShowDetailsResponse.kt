package com.example.ducius.responses

import com.example.ducius.model.ShowDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowDetailsResponse(

    @Json(name = "data")
    val show: ShowDetails? = null

)