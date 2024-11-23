package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    fun readHistory(): MutableList<Track>

    fun clearHistory()

    fun addNewTrack(track: Track)
}