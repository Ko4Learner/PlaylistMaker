package com.practicum.playlistmaker.search.data.repository


import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.mapper.TracksMapper
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.storage_tracks.TracksHistoryStorage
import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val tracksHistoryStorage: TracksHistoryStorage,
) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        if (response is TracksResponse) {
            emit(Resource.Success(TracksMapper.mapTrackResponse(response)))
        } else {
            emit(Resource.Error("Произошла сетевая ошибка"))
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

