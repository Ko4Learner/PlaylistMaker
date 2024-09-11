package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.TrackStorageDto
import com.practicum.playlistmaker.data.dto.TracksResponse
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.mapper.TracksMapper
import com.practicum.playlistmaker.data.storage.TracksHistoryStorage
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository


class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val tracksHistoryStorage: TracksHistoryStorage,
) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return TracksMapper.mapTrackResponse(response as TracksResponse)
        } else {
            return emptyList()
        }
    }

    override fun readHistory(): MutableList<Track> {
        return TracksMapper.mapTrackStorage(tracksHistoryStorage.read())
    }

    override fun clearHistory() {
        tracksHistoryStorage.clearHistory()
    }

    override fun addNewTrack(track: Track) {
        return tracksHistoryStorage.addNewTrack(TracksMapper.trackToTrackStorageDto(track))

    }
}

