package com.practicum.playlistmaker.search.ui.state

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface TracksState {

    data object Loading : TracksState

    data object Error : TracksState

    data object Empty : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState
}

