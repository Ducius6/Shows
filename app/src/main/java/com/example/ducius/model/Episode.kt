package com.example.ducius.model

import android.net.Uri
import java.io.Serializable

data class Episode(
    val title: String,
    val description: String,
    val seasonEpisode: String
) : Serializable