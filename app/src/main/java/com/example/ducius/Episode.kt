package com.example.ducius

import java.io.Serializable

data class Episode(
    val title: String,
    val description: String,
    val seasonEpisode: String
) : Serializable