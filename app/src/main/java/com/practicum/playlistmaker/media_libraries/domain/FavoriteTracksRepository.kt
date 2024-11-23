package com.practicum.playlistmaker.media_libraries.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun insertFavoriteTrack(track: Track)

    suspend fun deleteFavoriteTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>
}