package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override suspend fun readHistory(): MutableList<Track> {
        return repository.readHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun addNewTrack(track: Track) {
        repository.addNewTrack(track)
    }
}