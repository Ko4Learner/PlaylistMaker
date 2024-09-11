package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.Track

interface TracksRepository {
    fun searchTracks (expression: String): List<Track>

    fun readHistory(): MutableList<Track>

    fun clearHistory()

    fun addNewTrack(track: Track)
}

