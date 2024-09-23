package com.practicum.playlistmaker.search.domain.interactor.tracks_search_history

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository

class AddNewTrackSearchHistoryUseCase(private val tracksRepository: TracksRepository) {

    operator fun invoke(track: Track) {
        tracksRepository.addNewTrack(track)
    }
}