package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    suspend fun readHistory(): List<Track>

    fun clearHistory()

    fun addNewTrack(track: Track)
}