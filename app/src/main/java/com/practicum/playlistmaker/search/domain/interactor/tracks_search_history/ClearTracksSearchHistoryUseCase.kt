package com.practicum.playlistmaker.search.domain.interactor.tracks_search_history


import com.practicum.playlistmaker.search.domain.repository.TracksRepository

class ClearTracksSearchHistoryUseCase (private val tracksRepository: TracksRepository) {

    operator fun invoke() {
        tracksRepository.clearHistory()
    }
}