package com.practicum.playlistmaker.media_libraries.domain.repository

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun insertFavoriteTrack(track: Track)

    suspend fun deleteFavoriteTrack(trackId: Int)

    fun getFavoriteTracks(): Flow<List<Track>>
}