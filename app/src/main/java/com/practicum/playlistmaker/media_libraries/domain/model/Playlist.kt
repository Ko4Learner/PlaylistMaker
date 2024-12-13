package com.practicum.playlistmaker.media_libraries.domain.model

data class Playlist(
    val playlistId: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    val trackIdList: List<Int> = emptyList(),
    val tracksCount: Int = 0
)
