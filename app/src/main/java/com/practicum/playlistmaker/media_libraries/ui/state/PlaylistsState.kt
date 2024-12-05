package com.practicum.playlistmaker.media_libraries.ui.state

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist

sealed interface PlaylistsState {

    data object Empty : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState
}