package com.example.ducius.model

import com.example.ducius.responses.EpisodeResponse
import com.example.ducius.responses.ShowDetailsResponse

class CompleteShow(

    var episodeResponse: EpisodeResponse? = null,

    var showDetailsResponse: ShowDetailsResponse? = null,

    @Transient
    val isSuccessful: Boolean = true
)