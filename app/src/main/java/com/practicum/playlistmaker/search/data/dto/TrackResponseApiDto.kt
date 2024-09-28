package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackResponseApiDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTime: Long,
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
)