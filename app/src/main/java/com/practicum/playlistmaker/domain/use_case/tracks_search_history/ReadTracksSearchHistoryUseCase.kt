package com.practicum.playlistmaker.domain.use_case.tracks_search_history

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository


class ReadTracksSearchHistoryUseCase(private val tracksRepository: TracksRepository) {

    operator fun invoke(): MutableList<Track> {
        return tracksRepository.readHistory()
    }
}