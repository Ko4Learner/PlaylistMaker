package com.practicum.playlistmaker.domain.use_case.tracks_search

import com.practicum.playlistmaker.domain.model.Track

interface TracksSearchUseCase {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}
