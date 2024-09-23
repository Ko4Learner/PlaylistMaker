package com.practicum.playlistmaker.domain.use_case.tracks_search_history


import com.practicum.playlistmaker.domain.repository.TracksRepository

class ClearTracksSearchHistoryUseCase (private val tracksRepository: TracksRepository) {

    operator fun invoke() {
        tracksRepository.clearHistory()
    }
}