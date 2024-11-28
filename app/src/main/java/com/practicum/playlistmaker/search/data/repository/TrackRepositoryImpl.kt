package com.practicum.playlistmaker.search.data.repository


import com.practicum.playlistmaker.media_libraries.data.db.FavoriteTracksDatabase
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
    private val favoriteTracksDatabase: FavoriteTracksDatabase,
    private val tracksMapper: TracksMapper
) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response is TracksResponse) {
            val favoriteTracksId = favoriteTracksDatabase.trackDao().getFavoriteTracksId()
            val tracks = tracksMapper.mapTrackResponse(response)
            tracks.map { track ->
                if (favoriteTracksId.contains(track.trackId)) {
                    track.isFavorite = true
                }
            }
            emit(Resource.Success(tracks))
        } else {
            emit(Resource.Error("Произошла сетевая ошибка"))
        }
    }

    override suspend fun readHistory(): List<Track> {
        val favoriteTracksId = favoriteTracksDatabase.trackDao().getFavoriteTracksId()
        val tracks = tracksMapper.mapTrackStorage(tracksHistoryStorage.read())
        tracks.map { track ->
            if (favoriteTracksId.contains(track.trackId)) {
                track.isFavorite = true
            }
        }
        return tracks
    }

    override fun clearHistory() {
        tracksHistoryStorage.clearHistory()
    }

    override fun addNewTrack(track: Track) {
        tracksHistoryStorage.addNewTrack(tracksMapper.trackToTrackStorageDto(track))

    }
}

