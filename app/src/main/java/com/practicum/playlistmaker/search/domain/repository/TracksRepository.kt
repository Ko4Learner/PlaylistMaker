package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>

    suspend fun readHistory(): MutableList<Track>

    fun clearHistory()

    fun addNewTrack(track: Track)
}

