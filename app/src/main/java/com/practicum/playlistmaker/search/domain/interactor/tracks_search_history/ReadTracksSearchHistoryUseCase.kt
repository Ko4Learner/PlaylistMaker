package com.practicum.playlistmaker.search.domain.interactor.tracks_search_history

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository


class ReadTracksSearchHistoryUseCase(private val tracksRepository: TracksRepository) {

    operator fun invoke(): MutableList<Track> {
        return tracksRepository.readHistory()
    }
}