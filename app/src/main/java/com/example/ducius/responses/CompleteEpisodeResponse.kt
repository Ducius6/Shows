package com.example.ducius.responses

class CompleteEpisodeResponse(

    var mediaResponse: MediaResponse? = null,

    var postEpisodeResponse: PostEpisodeResponse? = null,

    @Transient
    var isSuccessful: Boolean = true
)