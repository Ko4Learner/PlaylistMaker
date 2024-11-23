package com.practicum.playlistmaker.media_libraries.data

import com.practicum.playlistmaker.media_libraries.data.db.FavoriteTracksDatabase
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_libraries.data.db.mapper.TrackDbMapper
import com.practicum.playlistmaker.media_libraries.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val favoriteTracksDatabase: FavoriteTracksDatabase,
    private val trackDbMapper: TrackDbMapper
) : FavoriteTracksRepository {

    override suspend fun insertFavoriteTrack(track: Track) {
        favoriteTracksDatabase.trackDao().insertFavoriteTrack(trackDbMapper.map(track))
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteTracksDatabase.trackDao().deleteFavoriteTrack(trackDbMapper.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = favoriteTracksDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(favoriteTracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbMapper.map(track) }
    }
}