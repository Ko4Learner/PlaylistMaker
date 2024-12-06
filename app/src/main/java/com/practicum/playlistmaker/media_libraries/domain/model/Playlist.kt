package com.practicum.playlistmaker.media_libraries.domain.model

data class Playlist(
    val playlistId: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    var trackIdList: List<Int> = emptyList(),
    var tracksCount: Int = 0
)
