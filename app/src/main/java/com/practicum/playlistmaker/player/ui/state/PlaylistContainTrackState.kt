package com.practicum.playlistmaker.player.ui.state

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist

interface PlaylistContainTrackState {

    data class StateContain(
        val playlist: Playlist,
    ) : PlaylistContainTrackState
    data class StateNotContain(
        val playlist: Playlist,
    ) : PlaylistContainTrackState
}