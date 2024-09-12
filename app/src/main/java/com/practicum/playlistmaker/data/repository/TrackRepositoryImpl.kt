package com.practicum.playlistmaker.data.repository


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
        return if (response.resultCode == 200) {
            TracksMapper.mapTrackResponse(response as TracksResponse)
        } else {
            emptyList()
        }
    }

    override fun readHistory(): MutableList<Track> {
        return TracksMapper.mapTrackStorage(tracksHistoryStorage.read())
    }

    override fun clearHistory() {
        tracksHistoryStorage.clearHistory()
    }

    override fun addNewTrack(track: Track) {
        tracksHistoryStorage.addNewTrack(TracksMapper.trackToTrackStorageDto(track))

    }
}

