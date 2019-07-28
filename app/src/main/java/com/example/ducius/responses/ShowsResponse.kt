package com.example.ducius.responses

import com.example.ducius.model.Show
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowsResponse(
    @Json(name = "data")
    val showsList: List<Show>? = arrayListOf<Show>(),

    @Transient
    var isSuccessful: Boolean = true
)