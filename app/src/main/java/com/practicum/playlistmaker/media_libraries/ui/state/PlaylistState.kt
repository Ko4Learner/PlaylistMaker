package com.practicum.playlistmaker.media_libraries.ui.state

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

data class PlaylistState(
    val playlist: Playlist,
    val trackList: List<Track>,
    val tracksTime: Int
)
