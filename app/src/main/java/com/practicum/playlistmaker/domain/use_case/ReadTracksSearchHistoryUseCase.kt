package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository


class ReadTracksSearchHistoryUseCase(private val tracksRepository: TracksRepository) {

    fun execute(): MutableList<Track> {
        return tracksRepository.readHistory()
    }


}