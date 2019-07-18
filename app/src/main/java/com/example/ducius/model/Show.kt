package com.example.ducius.model

import java.io.Serializable

data class Show(
    val ID: Int,
    val name: String,
    val airDate: String,
    val imageId: Int,
    val description: String,
    var listOfEpisodes: MutableList<Episode>
) : Serializable