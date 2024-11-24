package com.practicum.playlistmaker.media_libraries.ui.state

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FavoriteTracksState {
    data object Empty : FavoriteTracksState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksState
}